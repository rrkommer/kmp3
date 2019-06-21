package com.artefaktur.kmp3.intdb

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.artefaktur.kmp3.Mp3UsageDb
import com.artefaktur.kmp3.database.Media
import com.artefaktur.kmp3.database.Mp3UsageDB
import com.artefaktur.kmp3.database.Usage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Database(entities = arrayOf(MediaDO::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class IntDatabase : RoomDatabase() {
  abstract fun mediaDao(): MediaDao


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
//    dao.nukeTable()

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
}
