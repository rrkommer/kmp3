package com.artefaktur.kmp3

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.database.Title
import kotlinx.android.synthetic.main.fragment_title_detail.view.*
import android.text.method.LinkMovementMethod
import com.artefaktur.kmp3.database.Track


class TitleDetailFragment : BaseFragment(), PlayerStatusReceiver {

  lateinit var title: Title
  lateinit var trackFragment: TrackFragment
  private fun appendCol(sb: StringBuilder, key: String, value: String?): StringBuilder {
    if (value.isNullOrBlank()) {
      return sb
    }
    if (sb.length > 0) {
      sb.append(";")
    }
    sb.append(key).append(": ").append(value)
    return sb
  }

  fun createDetailView(view: View): Spannable {
    val sb = SpannableStringBuilder()
    var t2 = title.get(Title.TITEL2)
    if (t2.isNotBlank()) {
      t2 = t2 + "\n"
    } else {
      t2 = ""
    }

    val res = spannable {
      bold(size(2.0f, title.titleName + "\n")) +
              normal(t2) +
              clickSpan(size(1.5f, title.composerName + "\n")) {
                val composer = title.db.getComposerByName(title.composerName)
                if (composer != null) {
                  goMainLink(ComposerDetailFragment.newInstance(composer))
                } else {
                  toast("Komponist nicht gefunden")
                }
              } +
              "Medium: " +
              clickSpan(normal(title.media.name1 + "\n")) {
                goMainLink(MediaDetailFragment.newInstance(title.media))
              }

    }

    sb.append(res)
    val lb = StringBuilder()
    appendCol(lb, "Gruppe", title.get(Title.GRUPPE))
    appendCol(lb, "AF", title.get(Title.RECORDTYPE))
    appendCol(lb, "Jahr", title.get(Title.RECORDYEAR))
    appendCol(lb, "Einspielung", title.get(Title.EINSPIELUNG))

    if (lb.length > 0) {
      sb.append(lb.append("\n").toString())
    }
    val longComment = title.longComment
    if (longComment.isNullOrBlank() == false) {
      sb.append("  ").append(clickSpan(normal("[Anmerkung]\n")) {
        LongTextDialog.openDialog(view, longComment)
      })
    }
    val dirigent = title.get(Title.DIRIGENT)
    if (dirigent.isNotBlank()) {
      sb.append("Dirigent: ")
      sb.append(clickSpan(normal(dirigent)) {
        val dirigent = title.db.getDirigentByName(dirigent);
        if (dirigent != null) {
          goMainLink(GenericDetailFragment.newInstance(dirigent, DirigentItemContainer(listOf(dirigent))))
        }
      })
      sb.append("\n")
    }
    val orchester = title.db.getOrchesterByTitel(title)
    if (orchester.isNotEmpty()) {
      sb.append("Orchester: ")
      for (o in orchester) {
        sb.append(clickSpan(normal(o.name)) {
          goMainLink(GenericDetailFragment.newInstance(o, OrchesterItemContainer(listOf(o))))
        })
        sb.append(",")
      }
      sb.append("\n")
    }
    val interpreten = title.db.getInterpretByTitel(title)
    if (interpreten.isEmpty() == false) {
      sb.append("Interperten: ")
      for (interpret in interpreten) {
        val iname =
          if (interpret.instrument.isNotBlank()) interpret.name + ": " + interpret.instrument else interpret.name
        sb.append(clickSpan(normal(iname)) {
          goMainLink(GenericDetailFragment.newInstance(interpret, InterpretItemContainer(listOf(interpret))))
        })
        sb.append(",")
      }
      sb.append("\n")
    }
    return sb
  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_title_detail, container, false)

    view.title_detail_play_button.setOnClickListener {
      val tracks = Mp3Db.getDb().getTracksFromTitle(title)
      if (tracks.size > 0) {
        val mph = getMainActivity().mediaPlayerHolder
        mph.startPlaying(tracks)

      } else {
        Log.w("", "No Track")
      }
    }
    view.title_detail_name.text = createDetailView(view)
    view.title_detail_name.setMovementMethod(LinkMovementMethod.getInstance())

    doTrans {
      val tracks = Mp3Db.getDb().getTracksFromTitle(title)
      trackFragment = TrackFragment.newInstance(tracks, false)
      replace(R.id.title_detail_track_replacement, trackFragment)
    }
    return view
  }

  override fun onStartPlayTrack(track: Track) {
    trackFragment.onStartPlayTrack(track)
  }

  override fun onStopPlayTrack(track: Track) {
    trackFragment.onStopPlayTrack(track)

  }

  companion object {
    @JvmStatic
    fun newInstance(title: Title): TitleDetailFragment {
      val ret = TitleDetailFragment()
      ret.title = title
      return ret
    }
  }
}
