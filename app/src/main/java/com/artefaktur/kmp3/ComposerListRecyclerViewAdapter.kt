package com.artefaktur.kmp3

import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.artefaktur.kmp3.database.Composer

import kotlinx.android.synthetic.main.fragment_composerlist.view.*


class ComposerListRecyclerViewAdapter(
  mValues: List<Composer>
) : BaseRecycleAdapter<Composer, ComposerListRecyclerViewAdapter.ViewHolder>(mValues)
  , LetterPosCalculator {

  private val mOnClickListener: View.OnClickListener
  private var mLetters = LinkedHashMap<String, Int>()

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Composer
      val composerFrag = ComposerDetailFragment.newInstance(item)
      goMainLink(composerFrag)
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

  override fun getLetterPosition(letter: String): Int {
    if (mLetters.containsKey(letter)) {
      return mLetters[letter]!!
    }
    return -1
  }

  override fun getLetters(): Array<String> {
    return mLetters.keys.toTypedArray()
  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.fragment_composerlist, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = elements[position]
    val sb = SpannableStringBuilder()
    sb.append(item.name + " ").append(size(.7f, "(" + item.bornYear + "-" + item.diedYear + "), " + item.country))
    holder.mIdView.text = sb
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
