package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Media
import kotlinx.android.synthetic.main.fragment_media_detail.view.*
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.widget.ImageView
import android.widget.LinearLayout
import com.artefaktur.kmp3.database.Track


class MediaDetailFragment : BaseFragment(), PlayerStatusReceiver {
  lateinit var media: Media
  var imageZoomed = false
  var hasBackImage = false
  lateinit var trackFragment: TrackFragment
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
      trackFragment = TrackFragment.newInstance(tracks, true)
      replace(R.id.media_detail_track_replacement, trackFragment)
    }
    return view
  }

  override fun onStartPlayTrack(track: Track) {
    trackFragment.onStartPlayTrack(track)
  }

  override fun onStopPlayTrack(track: Track) {
    trackFragment.onStopPlayTrack(track)
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
      view.media_detail_bookletImageFront.setImageBitmap(bitmap)
      view.media_detail_bookletImageFront.setOnClickListener {
        zoomImage(view.media_detail_bookletImageFront, view.media_detail_bookletImageBack, false)
      }
    } else {
      view.media_detail_bookletImageFront.visibility = View.GONE
    }

    val bookletBack = media.getBookletFile(true)
    if (bookletBack != null) {
      val bitmap = BitmapFactory.decodeFile(bookletBack.absolutePath)
      view.media_detail_bookletImageBack.setImageBitmap(bitmap)
      view.media_detail_bookletImageBack.setOnClickListener {
        zoomImage(view.media_detail_bookletImageBack, view.media_detail_bookletImageFront, true)
      }
      hasBackImage = true
    } else {
      view.media_detail_bookletImageBack.visibility = View.GONE
    }
  }

  private fun zoomImage(image: ImageView, otherImage: ImageView, back: Boolean) {

    var layoutParams =
      if (imageZoomed == false)
      //LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        LinearLayout.LayoutParams(1000, 1000)
      else {
        val dim = getResources().getDimension(if (back) R.dimen.back_album_size else R.dimen.front_album_size)
        LinearLayout.LayoutParams(dim.toInt(), dim.toInt())
      }
    image.layoutParams = layoutParams
    if (imageZoomed == false) {
      otherImage.visibility = View.GONE
    } else {
      if (back == true || hasBackImage) {
        otherImage.visibility = View.VISIBLE
      }
    }
    imageZoomed = !imageZoomed
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
