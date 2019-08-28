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
import com.artefaktur.kmp3.intdb.IntDatabase
import org.apache.commons.lang3.StringUtils


class MediaListFragment : BaseRecycleSearchFragment<Media>() {
  var withFilter: Boolean = false
  lateinit var mediaList: List<Media>


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_medialist_list, container, false)
    val settings = getMainActivity().getSettings()

    updateMenuStatus()
    with(view as RecyclerView) {
      val mlva = MediaListRecyclerViewAdapter(mediaList)
      layoutManager = LinearLayoutManager(context)
      adapter = mlva
      var list = mediaList
      initElements(list, view, mlva)

      if (withFilter == true && (settings.onlyUnhearedMedia == true || settings.mediaStars != -1)) {
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
    menuFilter?.let { it.setVisible(true) }
    val sortMenu = mainActivity.menu.findItem(R.id.menu_sortmedia)
    sortMenu?.let { it.setVisible(true) }

  }

  private fun getRatingByMenu(id: Int): Int {
    return when (id) {
      R.id.menu_filter_all_rate -> -1
      R.id.menu_filter_rate5 -> 5
      R.id.menu_filter_rate4 -> 4
      R.id.menu_filter_rate3 -> 3
      R.id.menu_filter_rate2 -> 2
      R.id.menu_filter_rate1 -> 1
      R.id.menu_filter_rate0 -> 0
      else -> -1
    }
  }

  override fun handleMenuItem(mainActivity: MainActivity, item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_sortmedia_history -> {
        val settings = getMainActivity().getSettings()
        settings.mediaSortBy = MediaSort.History
        settings.onlyUnhearedMedia = false
        updateMenuStatus()
        refreshList()
      }
      R.id.menu_sortmedia_indb -> {
        val settings = getMainActivity().getSettings()
        settings.mediaSortBy = MediaSort.InDb
        updateMenuStatus()
        refreshList()
      }
      R.id.menu_filter_unheared -> {
        val settings = getMainActivity().getSettings()
        setUnheared(settings.onlyUnhearedMedia == false)
        return true
      }
      R.id.menu_filter_bookmarked -> {
        val settings = getMainActivity().getSettings()
        setBookmarked(settings.onlyBookmarked == false)
        return true
      }

      R.id.menu_filter_all_rate, R.id.menu_filter_rate5, R.id.menu_filter_rate4, R.id.menu_filter_rate3, R.id.menu_filter_rate2, R.id.menu_filter_rate1, R.id.menu_filter_rate0 -> {
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

  private fun setBookmarked(bookmarked: Boolean) {
    val settings = getMainActivity().getSettings()
    settings.onlyBookmarked = bookmarked
    getMainActivity().menu.findItem(R.id.menu_filter_bookmarked)?.let {
      it.setChecked(bookmarked)
    }
    refreshList()
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
      settings.mediaSortBy = MediaSort.InDb
      updateMenuStatus()
    }
    refreshList()
  }

  private fun updateMenuStatus() {
    val ma = getMainActivity()
    val settings = ma.getSettings()
    ma.menu.findItem(R.id.menu_filter_bookmarked)?.let {
      it.setChecked(settings.onlyBookmarked)
    }
    ma.menu.findItem(R.id.menu_filter_unheared)?.let {
      it.setChecked(settings.onlyUnhearedMedia)
    }
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
    val sortBy = settings.mediaSortBy
    ma.menu.findItem(R.id.menu_sortmedia_history)?.let {
      it.setChecked(sortBy == MediaSort.History)
    }
    ma.menu.findItem(R.id.menu_sortmedia_indb)?.let {
      it.setChecked(sortBy == MediaSort.InDb)
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
      0 -> {
        ma.menu.findItem(R.id.menu_filter_rate0)?.let {
          it.setChecked(true)
        }
      }
      else -> {
      }
    }

  }

  fun refreshList() {
    var list = originElements
    val ma = getMainActivity()
    val db = ma.intDb
    val settings = ma.getSettings()
    if (settings.mediaSortBy == MediaSort.History) {
      val histPks = getMainActivity().intDb.mediaDao().getMediaPkOrderByHistory()
      list = histPks.map { histpk -> originElements.find { it.pk == histpk } }.filterNotNull()
    } else {
      if (settings.onlyUnhearedMedia == true) {
        list = list.filter {
          db.getHearCount(it) <= 0
        }
      }

    }
    list = filterOnlyBookmark(settings, list, db)
    val filterStars = getMainActivity().getSettings().mediaStars
    if (filterStars != -1) {
      list = list.filter {
        val rt = db.getRating(it)
        rt == filterStars || rt == -1 && filterStars == 0
      }
    }


    filteredElements = list
    adapter.elements = filteredElements
    adapter.notifyDataSetChanged()
  }

  private fun filterOnlyBookmark(
    settings: AppSettings,
    list: List<Media>,
    db: IntDatabase
  ): List<Media> {
    var list1 = list
    if (settings.onlyBookmarked == true) {
      list1 = list1.filter {
        val bl = db.getBookmark(it)
        bl != null && bl != 0
      }
    }
    return list1
  }

  companion object {

    @JvmStatic
    fun newInstance(mediaList: List<Media>, withFilter: Boolean = false): MediaListFragment {
      val ret = MediaListFragment()
      ret.mediaList = mediaList
      ret.withFilter = withFilter
      return ret
    }
  }
}
