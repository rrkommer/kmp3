package com.artefaktur.kmp3.intdb

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.util.Log
import com.artefaktur.kmp3.Mp3UsageDb
import com.artefaktur.kmp3.database.CsvTable
import com.artefaktur.kmp3.database.Media
import com.artefaktur.kmp3.database.Mp3UsageDB
import com.artefaktur.kmp3.database.Usage
import com.artefaktur.kmp3.toast
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Database(entities = arrayOf(MediaDO::class), version = 2)
@TypeConverters(DateConverter::class)
abstract class IntDatabase : RoomDatabase() {
  abstract fun mediaDao(): MediaDao

  companion object {
    val MIGRATION_1_2 = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE MediaDO ADD COLUMN bookmark INTEGER")
        database.execSQL("ALTER TABLE MediaDO ADD COLUMN comment VARCHAR")

      }
    };
  }

  fun hearMedia(media: Media) {
    val dao = mediaDao()
    val m = dao.findByMediaId(media.pk)
    if (m != null) {
      m.lastHeared = Date()
      m.usage = m.usage + 1
      dao.updateMediaDOs(m)
    } else {
      dao.insertMediaDOs(MediaDO(media.pk, 1, Date()))
    }
  }

  fun unHearMedia(media: Media) {
    val dao = mediaDao()
    val m = dao.findByMediaId(media.pk)
    if (m != null) {
      if (m.usage > 0) {
        m.usage = m.usage - 1
        dao.updateMediaDOs(m)
      }
    }
  }

  fun setBookmark(media: Media, bookmark: Int?) {
    val dao = mediaDao()
    val m = dao.findByMediaId(media.pk)
    if (m != null) {
      m.bookmark = bookmark
      dao.updateMediaDOs(m)
    } else {
      val m = MediaDO(media.pk, 0, null)
      m.bookmark = bookmark;
      dao.insertMediaDOs(m)
    }
  }

  fun getBookmark(media: Media): Int? {
    val dao = mediaDao()
    val m = dao.findByMediaId(media.pk)
    if (m == null) {
      return null
    }
    return m.bookmark
  }

  fun setMediaComment(media: Media, text: String?) {
    val dao = mediaDao()
    val m = dao.findByMediaId(media.pk)
    if (m != null) {
      m.comment = text
      dao.updateMediaDOs(m)
    } else {
      val m = MediaDO(media.pk, 0, null)
      m.comment = text;
      dao.insertMediaDOs(m)
    }
  }

  fun rateMedia(media: Media, rating: Int) {
    val dao = mediaDao()
    val m = dao.findByMediaId(media.pk)
    if (m != null) {
      m.rating = rating
      dao.updateMediaDOs(m)
    } else {
      dao.insertMediaDOs(MediaDO(media.pk, 0, null, rating))
    }
  }

  fun getHearCount(media: Media): Int {
    return mediaDao().findByMediaId(media.pk)?.usage ?: 0
  }

  fun getRating(media: Media): Int {
    return mediaDao().findByMediaId(media.pk)?.rating ?: -1
  }

  fun initData() {
    val dao = mediaDao()

    if (dao.count() > 0) {
      return
    }
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val db = Mp3UsageDb.getInstance()
    for (rec in db.db.table) {
      val typ = rec[Usage.TYPE]
      if (Mp3UsageDB.MEDIA.equals(typ) == false) {
        continue
      }
      val pk = rec[Usage.PK]
      val count = rec[Usage.COUNT] ?: "0"
      val date = rec[Usage.DATE]
      var ddate: Date? = null
      if (date.isNullOrBlank() == false) {
        ddate = formatter.parse(date);
      }
      val icount = Integer.parseInt(count)
      dao.insertMediaDOs(MediaDO(pk, icount, ddate))

    }
  }

  fun importMediaUsage(file: File) {
    val table = CsvTable()
    table.load(file)
    // "MediaPK|Usage|lastHeared|Rating\n"
    val dao = mediaDao()
    var updated = 0
    var inserted = 0
    for (line in table.table.subList(1, table.table.size)) {
      if (line.size < 4) {
        continue
      }
      try {
        val pk = line[0]
        val usage = line[1]
        val lastHeared = line[2]
        val rating: String? = line[3]
        var bookmark: Int? = null
        var comment: String? = null
        if (line.size >= 6) {
          if (line[4].isNullOrBlank() == false) {
            bookmark = Integer.parseInt(line[4])
          }
          if (line[5].isNullOrBlank() == false) {
            comment = line[5]
          }
        }
        var m = dao.findByMediaId(pk)
        val wasNull = if (m == null) true else false
        if (m == null) {
          m = MediaDO(pk)
        }
        m.bookmark = bookmark
        m.comment = comment
        m.lastHeared = fromIsoString(lastHeared)
        if (rating.isNullOrBlank() == false) {
          m.rating = Integer.parseInt(rating)
        }
        if (wasNull) {
          dao.insertMediaDOs(m)
          ++inserted
        } else {
          dao.updateMediaDOs(m)
          ++updated
        }
      } catch (ex: Exception) {
        Log.e("usageimport", "Error importing line: ${ex.message}: $line", ex)
        toast("Error importing line: ${ex.message}: $line")
      }
    }
    toast("Usage imported. Updated: $updated, Inserted: $inserted")
//    it.write("MediaPK|Usage|lastHeared|Rating\n")
  }


}
