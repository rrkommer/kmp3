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

  protected fun resetMenu(mainActivity: MainActivity) {
    mainActivity.menu.findItem(R.id.menu_filter)?.let { it.setVisible(false) }
    mainActivity.menu.findItem(R.id.menu_rate)?.let { it.setVisible(false) }
    mainActivity.menu.findItem(R.id.menu_sortmedia)?.let { it.setVisible(false) }
  }

  open fun ajustMenu(mainActivity: MainActivity) {
    resetMenu(mainActivity)
  }

  open fun handleMenuItem(mainActivity: MainActivity, item: MenuItem): Boolean {
    return false
  }
}


