package com.artefaktur.kmp3

import android.graphics.BitmapFactory
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.artefaktur.kmp3.database.Composer


import com.artefaktur.kmp3.database.Media
import com.artefaktur.kmp3.intdb.toIsoString
import kotlinx.android.synthetic.main.fragment_media_detail.view.*

import kotlinx.android.synthetic.main.fragment_medialist.view.*


class MediaListRecyclerViewAdapter(
  mediaList: List<Media>
) : BaseRecycleAdapter<Media, MediaListRecyclerViewAdapter.ViewHolder>(mediaList) {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Media
      goMainLink(MediaDetailFragment.newInstance(item))
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.fragment_medialist, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = elements[position]
    val booklet = item.getBookletFile(false)
    if (booklet != null) {
      val bitmap = BitmapFactory.decodeFile(booklet.absolutePath)
      holder.bookletImageView.setImageBitmap(bitmap)
    }
    holder.mContentView.setText(getMediaList(item))
    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  private fun getMediaList(media: Media): Spannable {
    var name = media.name1
    if (name.contains("DummyCD")) {
      name = "DummyCD"
    }
    var ret = SpannableStringBuilder()
    ret.append(name)
    val name2 = media.name2
    if (name2.isNullOrBlank() == false) {
      ret.append(size(0.7f, ", " + name2))
    }
    getMainActivity().intDb.mediaDao().findByMediaId(media.pk)?.let {
      if (it.usage > 0) {
        ret.append(" " + size(0.7f, "(${it.lastHeared.toIsoString()}: ${it.usage})"))
      }
      if (it.rating > 0) {
        ret.append(" " + size(0.7f, " [${it.ratingAsStars()}]"))
      }
    }
    return ret
  }

  override fun getItemCount(): Int = elements.size

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val bookletImageView: ImageView = mView.medialist_booklet
    val mContentView: TextView = mView.content

    override fun toString(): String {
      return super.toString() + " '" + mContentView.text + "'"
    }
  }
}
