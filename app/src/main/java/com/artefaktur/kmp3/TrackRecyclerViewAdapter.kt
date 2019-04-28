package com.artefaktur.kmp3

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.artefaktur.kmp3.database.Track
import com.artefaktur.kmp3.player.MusicUtils

import kotlinx.android.synthetic.main.fragment_track.view.*


class TrackRecyclerViewAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder>() {

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
        val times =MusicUtils.formatSongDuration(item.time)
        holder.mIdView.text = "" + (position + 1) + " " + item.name + ": " + times
//        holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = tracks.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}