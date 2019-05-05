package com.artefaktur.kmp3

import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import com.artefaktur.kmp3.database.*


open abstract class GenericItemContainer<T : Any>
  (val items: List<T>) : LetterPosCalculator {


  private var mLetters = LinkedHashMap<String, Int>()

  init {
    generateLetters()
  }

  fun list(): List<T> {
    return items
  }

  fun size(): Int {
    return items.size
  }

  fun getItem(idx: Int): T {
    return items[idx]
  }

  abstract fun getListText(item: T): Spannable
  abstract fun getDetailText(item: T): Spannable
  abstract fun filter(c: T, text: String): Any
  abstract fun getFirstLetter(item: T): String
  abstract fun getMedienForItem(item: T): List<Media>
  abstract fun getTitleForItem(item: T): List<Title>

  private fun generateLetters() {
    try {
      items.forEachIndexed { index, item ->
        val fl = getFirstLetter(item)
        if (mLetters.containsKey(fl) == false) {
          mLetters[fl] = index
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun getLetterPosition(letter: String): Int {
    mLetters.get(letter)?.let {
      return it
    }
    return -1
  }

  override fun getLetters(): Array<String> {
    return mLetters.keys.toTypedArray()
  }

  fun createDetailContainer(item: T): Fragment {
    val ret = GenericDetailFragment.newInstance(item, this)
    return ret
  }
}


class OrchesterItemContainer(items: List<Orchester>) : GenericItemContainer<Orchester>(items) {

  override fun getListText(item: Orchester): Spannable {
    return SpannableString(item.name)
  }

  override fun getDetailText(item: Orchester): Spannable {
    return size(2.0f, item.name)
  }

  override fun filter(c: Orchester, text: String): Any {
    return c.name?.contains(text, ignoreCase = true) ?: false
  }

  override fun getFirstLetter(item: Orchester): String {
    val name = item.name.trim()
    return name.substring(0, 1)
  }

  override fun getMedienForItem(item: Orchester): List<Media> {
    val list = item.db.getMediaFromOrchester(item)
    return list
  }

  override fun getTitleForItem(item: Orchester): List<Title> {
    return item.db.getTitelsFromOrchester(item)
  }
}

class InterpretItemContainer(items: List<Interpret>) : GenericItemContainer<Interpret>(items) {

  override fun getListText(item: Interpret): Spannable {
    if (item.instrument.isNotBlank()) {
      return SpannableString(item.name + ": " + item.instrument)
    } else {
      return SpannableString(item.name)
    }
  }

  override fun getDetailText(item: Interpret): Spannable {
    return size(2.0F, getListText(item))
  }

  override fun filter(c: Interpret, text: String): Any {
    return c.name?.contains(text, ignoreCase = true) ?: false
  }

  override fun getFirstLetter(item: Interpret): String {
    val name = item.name.trim()
    return name.substring(0, 1)
  }

  override fun getMedienForItem(item: Interpret): List<Media> {
    val list = item.db.getMediaFromInterpret(item)
    return list
  }

  override fun getTitleForItem(item: Interpret): List<Title> {
    return item.db.getTitelsFromInterpret(item)
  }
}


class DirigentItemContainer(items: List<Dirigent>) : GenericItemContainer<Dirigent>(items) {

  override fun getListText(item: Dirigent): Spannable {
    return SpannableString(item.name)
  }

  override fun getDetailText(item: Dirigent): Spannable {
    return size(2.0F, getListText(item))
  }

  override fun filter(c: Dirigent, text: String): Any {
    return c.name?.contains(text, ignoreCase = true) ?: false
  }

  override fun getFirstLetter(item: Dirigent): String {
    val name = item.name.trim()
    return name.substring(0, 1)
  }

  override fun getMedienForItem(item: Dirigent): List<Media> {
    val list = item.db.getMediaFromDirigent(item)
    return list
  }

  override fun getTitleForItem(item: Dirigent): List<Title> {
    return item.db.getTitlesByDirigent(item)
  }
}