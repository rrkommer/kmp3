package com.artefaktur.kmp3


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Media
import kotlinx.android.synthetic.main.fragment_media_detail.view.*
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.database.Track
import com.artefaktur.kmp3.intdb.toIsoString
import com.artefaktur.kmp3.player.MusicUtils


class MediaDetailFragment : BaseFragment(), PlayerStatusReceiver {
  lateinit var media: Media
  var imageZoomed = false
  var hasBackImage = false
  lateinit var trackFragment: TrackFragment

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_media_detail, container, false)

    view.media_detail_play_button.setOnClickListener {
      val tracks = media.tracks
      if (tracks.size > 0) {
        val mph = getMainActivity().mediaPlayerHolder
        mph.startPlaying(tracks)
        getMainActivity().intDb.hearMedia(media)
//        Mp3UsageDb.getInstance().db.addMediaUsage(media.pk)
      } else {
        Log.w("", "No Track")
      }
    }
    view.media_detail_name.text = createDetailView(view)
    view.media_detail_name.setMovementMethod(LinkMovementMethod.getInstance())

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

  fun createDetailView(view: View): Spannable {
    val db = getMainActivity().intDb
    val composerList = media.titleList.map { it.composer }.toSet()

    val sb = SpannableStringBuilder()
    sb.append(bold(size(1.5f, media.name1 + "\n")))
    if (media.name2.isNotBlank() == true) {
      sb.append(bold(size(1.0f, media.name2 + "\n")))
    }
    if (media.name3.isNotBlank() == true) {
      sb.append(bold(size(1.0f, media.name2 + "\n")))
    }
    sb.append("Composer:")
    for (composer in composerList) {
      sb.append(" ")
      sb.append(clickSpan(normal(composer.name)) {
        goMainLink(ComposerDetailFragment.newInstance(composer))
      })
    }

    val mediaUsage = db.mediaDao().findByMediaId(media.pk)

    sb.append("\nLabel: ${media.label}")
    if (mediaUsage != null && mediaUsage.rating > 0) {
      sb.append("        [${mediaUsage.ratingAsStars()}]")
    }
    sb.append("\n")
    val mediaCount = media.getMediaCount()
    val laufZeit = getLaufzeit()
    sb.append("Medien: $mediaCount, Zeit: $laufZeit")
    if (mediaUsage != null && mediaUsage.bookmark != null) {
      sb.append("  ").append(clickSpan(normal("[-BOOK]")) {
        db.setBookmark(media, null)
        view.media_detail_name.text = createDetailView(view)
      })
    } else {
      sb.append("  ").append(clickSpan(normal("[+BOOK]")) {
        db.setBookmark(media, 1)
        view.media_detail_name.text = createDetailView(view)
      })
    }
    if (mediaUsage != null && mediaUsage.comment.isNullOrBlank() == false) {
      sb.append("\nComment: ").append(clickSpan(normal(mediaUsage.comment!!)) {
        openMediaDialog(view, mediaUsage.comment!!)
      })
    } else {
      sb.append("  ").append(clickSpan(normal("[+COMMENT]")) {
        openMediaDialog(view, "")
      })
    }
    sb.append("\n")
    val jpc = media.jpcId
    if (jpc.isNullOrBlank() == false) {
      sb.append(clickSpan(normal("JPC: $jpc\n")) {
        openURL("https://www.jpc.de/jpcng/home/detail/-/hnum/${jpc}?iampartner=Anon")
      })
    }
    sb.append("InDb: ${media.dateInDb}\n")
    mediaUsage?.let {
      if (it.usage > 0) {
        sb.append(spannable {
          normal("Last Played: ${it.lastHeared.toIsoString()}: ${it.usage}: ") +
                  clickSpan(normal("<Unuse>\n")) {
                    db.unHearMedia(media)
                  }
        })
      }
    }
//    val usedb = Mp3UsageDb.getInstance().db
//    usedb.getMediaUsage(media.pk)?.let {
//
//      }
//    }
    return sb
  }

  private fun openMediaDialog(view: View, comment: String) {
    val mcd = MediaCommentDialog()
    mcd.mediaDetailView = view
    mcd.comment = comment
    mcd.media = media
    mcd.mediaDetailFragment = this
    goMainLink(mcd)
  }

  fun getLaufzeit(): String {
    val tl = media.getTrackList()
    val sumTime = tl.fold(0L) { acc, track -> acc + track.getTimeFromMp3() }
    return MusicUtils.formatSongDuration(sumTime)
  }

  override fun ajustMenu(mainActivity: MainActivity) {
    resetMenu(mainActivity)
    val menuFilter = mainActivity.menu.findItem(R.id.menu_rate);
    menuFilter?.let { menuFilter.setVisible(true) }
  }

  override fun handleMenuItem(mainActivity: MainActivity, item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_rate1 -> rateMedia(1)
      R.id.menu_rate2 -> rateMedia(2)
      R.id.menu_rate3 -> rateMedia(3)
      R.id.menu_rate4 -> rateMedia(4)
      R.id.menu_rate5 -> rateMedia(5)
      else -> false
    }
  }

  private fun rateMedia(rate: Int): Boolean {
    getMainActivity().intDb.rateMedia(media, rate)
    return true
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
        val dim =
          getResources().getDimension(if (back) R.dimen.back_album_size else R.dimen.front_album_size)
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
