package com.artefaktur.kmp3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.database.Title
import kotlinx.android.synthetic.main.fragment_title_detail.view.*


class TitleDetailFragment : Fragment() {

    lateinit var title: Title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
