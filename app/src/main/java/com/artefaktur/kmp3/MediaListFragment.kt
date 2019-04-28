package com.artefaktur.kmp3

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Media
import org.apache.commons.lang3.StringUtils


class MediaListFragment : BaseRecycleSearchFragment<Media>() {
    lateinit var mediaList: List<Media>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_medialist_list, container, false)

        with(view as RecyclerView) {
            val mlva = MediaListRecyclerViewAdapter(mediaList)
            layoutManager = LinearLayoutManager(context)
            adapter = mlva
            initElements(mediaList, view, mlva)
        }
        return view
    }

    override fun filter(text: String): List<Media> {
        val found = mutableListOf<Media>()
        for (c in originElements) {
            if (StringUtils.containsIgnoreCase(c.name1, text) == true) {
                found.add(c)
            }
        }
        return found
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
