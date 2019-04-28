package com.artefaktur.kmp3

import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.artefaktur.kmp3.database.Composer

import kotlinx.android.synthetic.main.fragment_composerlist.view.*


class ComposerListRecyclerViewAdapter(
    mValues: List<Composer>
) : BaseRecycleAdapter<Composer, ComposerListRecyclerViewAdapter.ViewHolder>(mValues) {

    private val mOnClickListener: View.OnClickListener
    private var mLetters = LinkedHashMap<String, Int>()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Composer
            val composerFrag = ComposerDetailFragment.newInstance(item)
            pushClient(composerFrag, R.id.main_replacement)
        }
        generateLetters()
    }


    private fun generateLetters() {
        try {
            elements.forEachIndexed { index, artist ->
                if (!mLetters.containsKey(artist.name.substring(0, 1))) {
                    mLetters[artist.name.substring(0, 1)] = index
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLetterPosition(letter: String): Int {
        if (mLetters.containsKey(letter)) {
            return mLetters[letter]!!
        }
        return -1
    }

    fun getLetters(): Array<String> {
        return mLetters.keys.toTypedArray()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_composerlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = elements[position]
        holder.mIdView.text = item.name
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
