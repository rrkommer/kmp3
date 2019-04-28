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
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.database.Title

import com.artefaktur.kmp3.dummy.DummyContent
import com.artefaktur.kmp3.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TitleFragment.OnListFragmentInteractionListener] interface.
 */
class TitleFragment : Fragment() {


    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var titles: List<Title>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_title_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = TitleRecyclerViewAdapter(titles, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnListFragmentInteractionListener {

        fun onSelectedTitleInList(item: Title?)
    }

    companion object {

        @JvmStatic
        fun newInstance(titles: List<Title>): TitleFragment {
            val ret = TitleFragment().apply {
                arguments = Bundle().apply {

                }
            }
            ret.titles = titles
            return ret
        }
    }
}
