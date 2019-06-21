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
  var withFilter: Boolean = false
  lateinit var mediaList: List<Media>


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_medialist_list, container, false)
    val settings = getMainActivity().getSettings()

    updateMenuStatus()
    with(view as RecyclerView) {
      val mlva = MediaListRecyclerViewAdapter(mediaList)
      layoutManager = LinearLayoutManager(context)
      adapter = mlva
      var list = mediaList
      initElements(list, view, mlva)

      if (withFilter == true || settings.onlyUnhearedMedia == true || settings.mediaStars != -1) {
        refreshList()
      }
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

  private fun getRatingByMenu(id: Int): Int {
    return when (id) {
      R.id.menu_filter_all_rate -> -1
      R.id.menu_filter_rate5 -> 5
      R.id.menu_filter_rate4 -> 4
      R.id.menu_filter_rate3 -> 3
      R.id.menu_filter_rate2 -> 2
      R.id.menu_filter_rate1 -> 1
      else -> -1
    }
  }

  override fun handleMenuItem(mainActivity: MainActivity, item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_filter_unheared -> {
        val settings = getMainActivity().getSettings()
        setUnheared(settings.onlyUnhearedMedia == false)
        return true
      }

      R.id.menu_filter_all_rate, R.id.menu_filter_rate5, R.id.menu_filter_rate4, R.id.menu_filter_rate3, R.id.menu_filter_rate2, R.id.menu_filter_rate1 -> {
        val filterRatings = getRatingByMenu(item.itemId)
        getMainActivity().getSettings().mediaStars = filterRatings
        if (filterRatings != -1) {
          getMainActivity().getSettings().onlyUnhearedMedia = false
        }
        updateMenuStatus()
        refreshList()
      }
    }
    return false
  }

  private fun setUnheared(unheared: Boolean) {
    val settings = getMainActivity().getSettings()

    settings.onlyUnhearedMedia = unheared
    getMainActivity().menu.findItem(R.id.menu_filter_unheared)?.let {
      it.setChecked(unheared)
      it.setTitle(if (settings.onlyUnhearedMedia) "WithListened" else "Unheared")
    }
    if (unheared == true) {
      settings.mediaStars = -1
      updateMenuStatus()
    }
    refreshList()
  }

  private fun updateMenuStatus() {
    val ma = getMainActivity()
    val settings = ma.getSettings()
    ma.menu.findItem(R.id.menu_filter_rate5)?.let {
      it.setChecked(false)
    }
    ma.menu.findItem(R.id.menu_filter_rate4)?.let {
      it.setChecked(false)
    }
    ma.menu.findItem(R.id.menu_filter_rate3)?.let {
      it.setChecked(false)
    }
    ma.menu.findItem(R.id.menu_filter_rate2)?.let {
      it.setChecked(false)
    }
    ma.menu.findItem(R.id.menu_filter_rate1)?.let {
      it.setChecked(false)
    }
    ma.menu.findItem(R.id.menu_filter_all_rate)?.let {
      it.setChecked(false)
    }
    when (settings.mediaStars) {
      -1 -> {
        ma.menu.findItem(R.id.menu_filter_all_rate)?.let {
          it.setChecked(true)
        }
      }
      5 -> {
        ma.menu.findItem(R.id.menu_filter_rate5)?.let {
          it.setChecked(true)
        }
      }
      4 -> {
        ma.menu.findItem(R.id.menu_filter_rate4)?.let {
          it.setChecked(true)
        }
      }
      3 -> {
        ma.menu.findItem(R.id.menu_filter_rate3)?.let {
          it.setChecked(true)
        }
      }
      2 -> {
        ma.menu.findItem(R.id.menu_filter_rate2)?.let {
          it.setChecked(true)
        }
      }
      1 -> {
        ma.menu.findItem(R.id.menu_filter_rate1)?.let {
          it.setChecked(true)
        }
      }
      else -> {
      }
    }
  }

  fun refreshList() {
    var list = originElements
    val db = getMainActivity().intDb
    if (settings.onlyUnhearedMedia == true) {
      list = list.filter {
        db.getHearCount(it) <= 0
      }
    }
    val filterStars = getMainActivity().getSettings().mediaStars
    if (filterStars != -1) {
      list = list.filter {
        db.getRating(it) == filterStars
      }
    }
    filteredElements = list
    adapter.elements = filteredElements
    adapter.notifyDataSetChanged()
  }

  companion object {

    @JvmStatic
    fun newInstance(mediaList: List<Media>, withFilter: Boolean = false): MediaListFragment {
      val ret = MediaListFragment()
      ret.mediaList = mediaList
      ret.withFilter = true
      return ret
    }
  }
}
