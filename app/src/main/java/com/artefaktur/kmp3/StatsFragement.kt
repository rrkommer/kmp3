package com.artefaktur.kmp3


import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.database.Track
import kotlinx.android.synthetic.main.fragment_stats.view.*
import java.io.File
import java.io.UncheckedIOException
import java.lang.Exception
import android.os.Handler
import android.os.Message
import android.widget.TextView


/**
 * A simple [Fragment] subclass.
 *
 */
class StatsFragement : Fragment() {
  companion object {
    var handler: Handler = object : Handler() {
      override fun handleMessage(msg: Message) {
        toast("Stats Berechnet")
      }
    }

    enum class Status {
      UnCalc,
      InCalc,
      Calculated
    }

    var status = Status.UnCalc
    var sumSize: Long = 0
    // seconds
    var sumTime: Long = 0
    var numCalculated= 0

    fun startCalc() {
      if (status != Status.UnCalc) {
        return
      }
      status = Status.InCalc
      val mythread = object : Thread() {
        override fun run() {
          val db = Mp3Db.getDb()
          var sumTimeC = 0L
          var sumSizeC = 0L
          for (tr in db.tracks.table) {
            try {

              val track = Track(db, tr)
              val metaRetriever = MediaMetadataRetriever()
              val filep = track.getMp3Path().getAbsolutePath()
              metaRetriever.setDataSource(track.getMp3Path().getAbsolutePath())
              val duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
              sumTimeC += java.lang.Long.parseLong(duration)
              sumSizeC += File(filep).length()
            } catch (ex: Exception) {
            }
            ++numCalculated
          }
          StatsFragement.sumSize = sumSizeC
          StatsFragement.sumTime = sumTimeC
          StatsFragement.status = Companion.Status.Calculated
          handler.sendMessage(Message())
        }
      }
      mythread.start()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_stats, container, false)
    val sb = SpannableStringBuilder()
    val db = Mp3Db.getDb()
    sb.append("Composer: ${db.composers.size}\n")
    val mediacount = db.media.fold(0) { cur, med -> cur + med.mediaCount }

    sb.append("Medien: ${db.media.size}, Datenträger: ${mediacount}\n")
    sb.append("Titel: ${db.title.size()}\n")
//    val sumSize = db.tracks.table.fold(0L) { cur, trackRow -> cur + Track(db, trackRow).size}
//    val sumtime = db.tracks.table.fold(0L) { cur, trackRow -> cur + Track(db, trackRow).trackTime }
//    val sumtime = db.tracks.table.fold(0L) { cur, trackRow -> cur + Track(db, trackRow).timeFromMp3 }
//    val sumHours = sumtime / (60 * 60)
//    val sumDays = sumtime / (60 * 60 * 24)
//    val restHours = (sumtime % (60 * 60 * 24)) / (60 * 60)
//    val sumMb = sumSize / (1024 * 124)
//    val sumGb = sumMb / 1024
    startCalc()
    sb.append("Tracks: ${db.tracks.size()}\n")
    if (status == Companion.Status.Calculated) {
      val sumHours = sumTime / (60 * 60)
      val sumDays = sumTime / (60 * 60 * 24)
      val restHours = (sumTime % (60 * 60 * 24)) / (60 * 60)
      val sumMb = sumSize / (1024 * 124)
      val sumGb = sumMb / 1024
      sb.append(" Days: ${sumDays}:${restHours} or ${sumHours} Total Hours. Size: ${sumGb} GB\n")
    } else {
      sb.append(" In Calc: ${numCalculated} / ${db.tracks.table.size}\n")
    }
    sb.append("Orchester: ${db.orchester.size}\n")
    sb.append("Dirigenten: ${db.dirigenten.size}\n")
    sb.append("Interpreten: ${db.interpreten.size}\n")

    view.stats_details.text = sb
    return view
  }

}
