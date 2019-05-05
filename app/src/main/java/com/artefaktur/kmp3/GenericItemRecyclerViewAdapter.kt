package com.artefaktur.kmp3

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.artefaktur.kmp3.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_genericitem.view.*


class GenericItemRecyclerViewAdapter<T : Any>(
  private val itemContainer: GenericItemContainer<T>
) : BaseRecycleAdapter<T, GenericItemRecyclerViewAdapter<T>.ViewHolder>(itemContainer.items),
  LetterPosCalculator {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as T
      val frag  = itemContainer.createDetailContainer(item)
      goMainLink(frag)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.fragment_genericitem, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = elements[position]
    holder.textLine.text = itemContainer.getListText(item)

    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getLetterPosition(letter: String): Int {
    return itemContainer.getLetterPosition(letter)
  }

  override fun getLetters(): Array<String> {
    return itemContainer.getLetters()
  }

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val textLine: TextView = mView.generic_item_list_content

    override fun toString(): String {
      return super.toString() + " '" + textLine.text + "'"
    }
  }
}
