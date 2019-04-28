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

import com.artefaktur.kmp3.dummy.DummyContent
import com.artefaktur.kmp3.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ComposerListFragment.OnListFragmentInteractionListener] interface.
 */
class ComposerListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var composers: List<Composer>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_composerlist_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = GridLayoutManager(context, 2)

                adapter = MyComposerListRecyclerViewAdapter(Mp3Db.getDb().composers, listener)
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
        fun onSelectComposer(item: Composer?)
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
