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
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.text.Spannable
import android.text.SpannableStringBuilder


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
        view.media_detail_name.text = createDetailView()

        showBooklet(view)
        doTrans {
            val tracks = media.tracks
            val tf = TrackFragment.newInstance(tracks, true)
            replace(R.id.media_detail_track_replacement, tf)
        }
        return view
    }

    fun createDetailView(): Spannable {
        val sb = SpannableStringBuilder()
        sb.append(bold(size(2.0f, media.name1 + "\n")))
        if (media.name2.isNotBlank() == true) {
            sb.append(bold(size(1.5f, media.name2 + "\n")))
        }
        if (media.name3.isNotBlank() == true) {
            sb.append(bold(size(1.5f, media.name2 + "\n")))
        }
        sb.append("Label: ${media.label}\n")
        sb.append("InDb: ${media.dateInDb}\n")
        return sb

    }

    private fun showBooklet(view: View) {
        val booklet = media.getBookletFile(false)
        if (booklet != null) {
            val bitmap = BitmapFactory.decodeFile(booklet.absolutePath)
            view.media_detail_bookletImage.setImageBitmap(bitmap)
        } else {
            view.media_detail_bookletImage.visibility = View.GONE
        }
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
