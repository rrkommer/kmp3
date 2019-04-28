package com.artefaktur.kmp3

import android.support.v7.widget.RecyclerView
import com.artefaktur.kmp3.database.Composer

open abstract class BaseRecycleSearchFragment<T>
    : BaseFragment(), FilterableFragment {
    lateinit var originElements: List<T>
    protected lateinit var filteredElements: List<T>
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: BaseRecycleAdapter<T, out RecyclerView.ViewHolder>


    abstract fun filter(text: String): List<T>
    override fun onResetSearch() {
        filteredElements = originElements
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSearch(text: String) {
        if (::adapter.isInitialized == false) {
            return
        }
        filteredElements = filter(text)
        adapter.elements = filteredElements
        adapter.notifyDataSetChanged()
    }

    fun initElements(
        elements: List<T>, recyclerView: RecyclerView, adapter: BaseRecycleAdapter<T, out RecyclerView.ViewHolder>
    ) {
        originElements = elements
        filteredElements = elements
        this.recyclerView = recyclerView
        this.adapter = adapter
    }
}