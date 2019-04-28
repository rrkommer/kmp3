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


class TitleDetailFragment : Fragment() {

    lateinit var title: Title

    fun createDetailView(): Spannable {
        val sb = SpannableStringBuilder()
        val res = spannable {
            bold(size(2.0f, title.titleName + "\n")) +
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
        if (title.get(Title.DIRIGENT).isNotBlank()) {
            sb.append("Dirigent: " + title.get(Title.DIRIGENT) + "\n")
        }
        val interpreten = title.db.getInterpretByTitel(title)
        if (interpreten.isEmpty() == false) {
            sb.append("Interperten: \n")
            for (interpret in interpreten) {
                sb.append(interpret.name)
                if (interpret.instrument.isNotBlank()) {
                    sb.append(": ").append(interpret.instrument)
                }
                sb.append("\n")
            }
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
        view.title_detail_name.text = createDetailView()
        view.title_detail_name.setMovementMethod(LinkMovementMethod.getInstance())

        doTrans {
            val tracks = Mp3Db.getDb().getTracksFromTitle(title)
            val tf = TrackFragment.newInstance(tracks, false)
            replace(R.id.title_detail_track_replacement, tf)
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
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
