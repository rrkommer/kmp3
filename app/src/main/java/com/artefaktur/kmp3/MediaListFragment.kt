package com.artefaktur.kmp3

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Media


class MediaListFragment : Fragment() {
    lateinit var mediaList: List<Media>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_medialist_list, container, false)

        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MediaListRecyclerViewAdapter(mediaList)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaList: List<Media>): MediaListFragment {
            val ret = MediaListFragment()
            ret.mediaList = mediaList
            return ret
        }
    }
}
