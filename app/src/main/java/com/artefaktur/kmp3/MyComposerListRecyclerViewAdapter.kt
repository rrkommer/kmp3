package com.artefaktur.kmp3

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.artefaktur.kmp3.ComposerListFragment.OnListFragmentInteractionListener
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_composerlist.view.*


class MyComposerListRecyclerViewAdapter(
    private val mValues: List<Composer>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyComposerListRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Composer
            mListener?.onSelectComposer(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_composerlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.name
//        holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
