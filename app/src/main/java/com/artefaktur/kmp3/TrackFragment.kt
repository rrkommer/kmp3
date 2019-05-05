package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Track

import com.artefaktur.kmp3.dummy.DummyContent
import com.artefaktur.kmp3.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_track.view.*

class TrackFragment : Fragment(), PlayerStatusReceiver {

  lateinit var tracks: List<Track>
  var showTitles: Boolean = true
  lateinit var trackViewAdapter: TrackRecyclerViewAdapter
  lateinit var recyclerView: RecyclerView
  lateinit var layoutManager: LinearLayoutManager

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    recyclerView = inflater.inflate(R.layout.fragment_track_list, container, false) as RecyclerView
    layoutManager = LinearLayoutManager(context)
    recyclerView.layoutManager = layoutManager
    trackViewAdapter = TrackRecyclerViewAdapter(tracks, showTitles)
    recyclerView.adapter = trackViewAdapter
    return recyclerView
  }

  private fun indexOfTrack(track: Track): Int {
    val ret = tracks.indexOf(track)
    return ret
  }

  override fun onStartPlayTrack(track: Track) {
    setSelected(track, true)
  }

  override fun onStopPlayTrack(track: Track) {
    setSelected(track, false)
  }

  fun setSelected(track: Track, select: Boolean) {
    val idx = indexOfTrack(track)
    if (idx == -1) {
      Log.i("Track", "Cannot find track in list ${track.name}; select: ${select}")
      return
    }
    Log.i("Track", "REFRESH Find track in list ${track.name}; select: ${select}")
    trackViewAdapter.notifyItemChanged(idx)
    recyclerView.setAdapter(null);
    recyclerView.setLayoutManager(null);
    recyclerView.setAdapter(trackViewAdapter);
    recyclerView.setLayoutManager(layoutManager);
    trackViewAdapter.notifyDataSetChanged();
  }


  companion object {
    @JvmStatic
    fun newInstance(tracks: List<Track>, showTitles: Boolean): TrackFragment {
      val ret = TrackFragment()
      ret.tracks = tracks
      ret.showTitles = showTitles
      return ret
    }
  }
}
