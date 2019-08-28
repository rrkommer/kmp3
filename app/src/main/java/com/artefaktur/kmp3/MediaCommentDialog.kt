package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Media
import kotlinx.android.synthetic.main.fragment_media_comment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_media_detail.view.*

/**
 * A simple [Fragment] subclass.
 */
class MediaCommentDialog(

) : Fragment() {
  lateinit var mediaDetailFragment: MediaDetailFragment
  lateinit var mediaDetailView: View
  lateinit var comment: String
  lateinit var media: Media
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {


    val view = inflater.inflate(R.layout.fragment_media_comment_dialog, container, false)
    view.media_comment_textarea.setText(comment)
    view.media_comment_dialog_cancel.setOnClickListener {
      closeDialog()
    }
    view.media_comment_dialog_save.setOnClickListener {
      val db = getMainActivity().intDb
      db.setMediaComment(media, view.media_comment_textarea.text.toString())
      closeDialog()
//      view.media_detail_name.text = mediaDetailFragment.createDetailView(view)
    }
    return view
  }

  fun closeDialog() {
    getMainActivity().onBackPressed()
  }

}
