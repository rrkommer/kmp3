package com.artefaktur.kmp3

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Media
import org.apache.commons.lang3.StringUtils


class MediaListFragment : BaseRecycleSearchFragment<Media>() {
  lateinit var mediaList: List<Media>

  var filterUnheared = false
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_medialist_list, container, false)

    with(view as RecyclerView) {
      val mlva = MediaListRecyclerViewAdapter(mediaList)
      layoutManager = LinearLayoutManager(context)
      adapter = mlva
      initElements(mediaList, view, mlva)
    }
    return view
  }

  override fun filter(text: String): List<Media> {
    val found = mutableListOf<Media>()
    for (c in originElements) {
      if (StringUtils.containsIgnoreCase(c.name1, text) == true) {
        found.add(c)
      }
    }
    return found
  }

  override fun ajustMenu(mainActivity: MainActivity) {
    resetMenu(mainActivity)
    val menuFilter = mainActivity.menu.findItem(R.id.menu_filter)
    menuFilter?.let { menuFilter.setVisible(true) }
  }

  override fun handleMenuItem(mainActivity: MainActivity, item: MenuItem): Boolean {
    if (item.itemId == R.id.menu_filter_unheared) {
      filterUnheared = !filterUnheared
      mainActivity.menu.findItem(R.id.menu_filter_unheared)?.let {
        it.setTitle(if (filterUnheared) "WithListened" else "Unheared")
      }
      refreshList()
      return true
    }
    return false
  }

  fun refreshList() {
    var list = originElements
    if (filterUnheared == true) {
      val db = getMainActivity().intDb

      list = list.filter {
        db.getHearCount(it) <= 0
      }
    }
    filteredElements = list
    adapter.elements = filteredElements
    adapter.notifyDataSetChanged()
  }

  companion object {
    @JvmStatic
    fun newInstance(mediaList: List<Media>): MediaListFragment {
      val ret = MediaListFragment()
      ret.mediaList = mediaList
      return ret
    }
  }
}
