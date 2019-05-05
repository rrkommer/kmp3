package com.artefaktur.kmp3

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_composerlist_list.view.*
import kotlinx.android.synthetic.main.fragment_genericitem_list.*
import kotlinx.android.synthetic.main.fragment_genericitem_list.view.*


class GenericItemFragment<T : Any> : BaseRecycleSearchFragment<T>() {


  lateinit var itemContainer: GenericItemContainer<T>
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_genericitem_list, container, false)
    recyclerView = view.generic_recyclerview
    val layoutManager = LinearLayoutManager(context)
    recyclerView.layoutManager = layoutManager
    val genericViewAdapter = GenericItemRecyclerViewAdapter(itemContainer)
    recyclerView.adapter = genericViewAdapter
    initElements(itemContainer.items, recyclerView, genericViewAdapter)
    recyclerView.afterMeasured {
      val vw = view.generic_wave_view
      if (recyclerView.computeVerticalScrollRange() > height) {
        vw.setOnWaveTouchListenerForComposer(recyclerView, genericViewAdapter, layoutManager)
        vw.letters = genericViewAdapter.getLetters()
      } else {
        vw.visibility = android.view.View.GONE
      }
    }
    return view
  }

  override fun filter(text: String): List<T> {
    val found = mutableListOf<T>()
    for (c in originElements) {
      if (itemContainer.filter(c, text) == true) {
        found.add(c)
      }
    }
    return found
  }

  companion object {
    @JvmStatic
    fun <T : Any> newInstance(itemContainer: GenericItemContainer<T>): GenericItemFragment<T> {
      val ret = GenericItemFragment<T>()
      ret.itemContainer = itemContainer
      return ret
    }
  }
}
