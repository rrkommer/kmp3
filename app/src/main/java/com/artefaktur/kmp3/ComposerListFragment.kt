package com.artefaktur.kmp3

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Mp3Db
import kotlinx.android.synthetic.main.fragment_composerlist_list.view.*
import org.apache.commons.lang3.StringUtils


class ComposerListFragment : BaseRecycleSearchFragment<Composer>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_composerlist_list, container, false)
        recyclerView = view.composerlist_recycler
        val cadapter = ComposerListRecyclerViewAdapter(Mp3Db.getDb().composers)
        adapter = cadapter
        var linLayout = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        initElements(originElements, recyclerView, cadapter)
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


    override fun filter(text: String): List<Composer> {
        val found = mutableListOf<Composer>()
        for (c in originElements) {
            if (StringUtils.containsIgnoreCase(c.name, text) == true) {
                found.add(c)
            }
        }
        return found
    }

    companion object {

        @JvmStatic
        fun newInstance(composers: List<Composer>): ComposerListFragment {
            val ret = ComposerListFragment()
            ret.originElements = composers
            return ret
        }
    }
}
