package com.artefaktur.kmp3

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.artefaktur.kmp3.database.Track
import com.artefaktur.kmp3.player.MusicUtils

import kotlinx.android.synthetic.main.fragment_track.view.*


class TrackRecyclerViewAdapter(
  private val tracks: List<Track>,
  private val showTitles: Boolean
) : RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder>(),
  PlayerStatusReceiver {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Track
      getMainActivity().mediaPlayerHolder.startPlaying(item, tracks)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.fragment_track, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = tracks[position]
    val renderTitle =
      showTitles && (position == 0 || (tracks[position - 1].title.pk != item.title.pk))
    if (renderTitle == true) {
      holder.track_list_titel.visibility = View.VISIBLE
      holder.track_list_titel.text = item.title.titleName
      holder.track_list_titel.setOnClickListener {
        val titleFrag = TitleDetailFragment.newInstance(item.title)
        goMainLink(titleFrag)
      }
    } else {
      holder.track_list_titel.visibility = View.GONE
    }
    val times = MusicUtils.formatSongDuration(item.timeFromMp3)
    holder.item_number.text = "" + (position + 1)
    val curPlayingTrack = currentTrackPlaying()
//    val noncur = "<null>"
    val curPlaying = curPlayingTrack == item
//    Log.i(
//      "Track",
//      "Track painting is ${item.name}; idx=${position}; selected: ${holder.mView.isSelected}; curPlaying: ${curPlayingTrack?.name
//        ?: noncur}"
//    )


    if (curPlaying == false) {
      holder.content.text = item.name
    } else {
      holder.content.text = italic(bold(item.name))
    }
    holder.track_time.text = times
    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getItemCount(): Int = tracks.size
  override fun onStartPlayTrack(track: Track) {
    val idx = tracks.indexOf(track)
    if (idx == -1) {
      return
    }
  }

  override fun onStopPlayTrack(track: Track) {
    val idx = tracks.indexOf(track)
    if (idx == -1) {
      return
    }

  }

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val item_number: TextView = mView.item_number
    val track_list_titel = mView.track_list_titel
    val content: TextView = mView.content
    val track_time: TextView = mView.track_time
    override fun toString(): String {
      return super.toString() + " '" + content.text + "'"
    }
  }
}