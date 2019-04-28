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
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Mp3Db


class ComposerListFragment : Fragment() {
    private lateinit var composers: List<Composer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_composerlist_list, container, false) as RecyclerView
        with(view) {
            layoutManager = LinearLayoutManager(context)// GridLayoutManager(context, 2)
            adapter = ComposerListRecyclerViewAdapter(Mp3Db.getDb().composers)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {

        @JvmStatic
        fun newInstance(composers: List<Composer>): ComposerListFragment {
            val ret = ComposerListFragment()
            ret.composers = composers
            return ret
        }
    }
}
