package com.artefaktur.kmp3

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Track

import com.artefaktur.kmp3.dummy.DummyContent
import com.artefaktur.kmp3.dummy.DummyContent.DummyItem

class TrackFragment : Fragment() {

    lateinit var tracks: List<Track>
    var showTitles: Boolean = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_track_list, container, false) as RecyclerView
        view.layoutManager = LinearLayoutManager(context)
        view.adapter = TrackRecyclerViewAdapter(tracks, showTitles)
        return view
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
