package com.artefaktur.kmp3

import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.artefaktur.kmp3.database.Composer


import com.artefaktur.kmp3.database.Title

import kotlinx.android.synthetic.main.fragment_title.view.*


class TitleRecyclerViewAdapter(
    mValues: List<Title>
) : BaseRecycleAdapter<Title, TitleRecyclerViewAdapter.ViewHolder>(mValues) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Title
            doTrans {
                val titleFrag = TitleDetailFragment.newInstance(item)
                doTrans {
                    replace(R.id.main_replacement, titleFrag)
                    addToBackStack(null)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_title, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = elements[position]
        holder.mIdView.text = item.titleName
//        holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
