package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Media
import com.artefaktur.kmp3.database.Mp3Db
import kotlinx.android.synthetic.main.fragment_media_detail.view.*
import kotlinx.android.synthetic.main.fragment_title_detail.view.*


class MediaDetailFragment : Fragment() {
    lateinit var media: Media

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_media_detail, container, false)

        view.media_detail_play_button.setOnClickListener {
            val tracks = media.tracks
            if (tracks.size > 0) {
                val mph = getMainActivity().mediaPlayerHolder
                mph.startPlaying(tracks)

            } else {
                Log.w("", "No Track")
            }
        }
        view.media_detail_name.text = media.listName
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(media: Media): MediaDetailFragment {
            val ret = MediaDetailFragment()
            ret.media = media
            return ret
        }
    }
}
