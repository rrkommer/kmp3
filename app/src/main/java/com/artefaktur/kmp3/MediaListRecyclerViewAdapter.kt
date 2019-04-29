package com.artefaktur.kmp3

import android.graphics.BitmapFactory
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.artefaktur.kmp3.database.Composer


import com.artefaktur.kmp3.database.Media
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
    holder.mContentView.text = item.listName
    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
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
