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
import com.artefaktur.kmp3.database.Title


class TitleListFragment : Fragment() {
    private lateinit var titles: List<Title>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_title_list, container, false)
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = TitleRecyclerViewAdapter(titles)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(titles: List<Title>): TitleListFragment {
            val ret = TitleListFragment()
            ret.titles = titles
            return ret
        }
    }
}
