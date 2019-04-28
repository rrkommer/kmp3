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
import kotlinx.android.synthetic.main.fragment_composerlist.view.*
import kotlinx.android.synthetic.main.fragment_composerlist_list.view.*


class ComposerListFragment : Fragment() {
    private lateinit var composers: List<Composer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_composerlist_list, container, false)

        val recyclerView = view.composerlist_recycler

        val cadapter = ComposerListRecyclerViewAdapter(Mp3Db.getDb().composers)
        var linLayout = LinearLayoutManager(context)
        recyclerView.adapter = cadapter

        view.afterMeasured {
            if (recyclerView.computeVerticalScrollRange() > height) {
                val vw = composer_wave_view as WaveView
                vw.setOnWaveTouchListenerForComposer(recyclerView, cadapter, linLayout)
                vw.letters = cadapter.getLetters()
            } else {
                view.composer_wave_view.visibility = View.GONE
            }
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
