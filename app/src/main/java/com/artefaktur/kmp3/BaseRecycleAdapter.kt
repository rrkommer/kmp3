package com.artefaktur.kmp3

import android.support.v7.widget.RecyclerView

open abstract class BaseRecycleAdapter<T, VH : RecyclerView.ViewHolder>(
    var elements: List<T>
) : RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int = elements.size
}
