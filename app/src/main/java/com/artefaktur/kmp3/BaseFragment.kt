package com.artefaktur.kmp3

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import android.view.View

open class BaseFragment : Fragment() {
  var main: MainActivity? = null

  fun manager(): FragmentManager {
    return main!!.supportFragmentManager
  }

  open fun onSearch(text: String) {

  }

  open fun onResetSearch() {

  }

  open fun ajustMenu(mainActivity: MainActivity) {
    val menuFilter = mainActivity.menu.findItem(R.id.menu_filter);
    menuFilter?.let { menuFilter.setVisible(false) }
  }

  open fun handleMenuItem(mainActivity: MainActivity, item: MenuItem): Boolean {
    return false
  }
}


