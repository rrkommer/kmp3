package com.artefaktur.kmp3

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.artefaktur.ourmp3.player.MediaPlayerHolder

import kotlinx.android.synthetic.main.activity_main.toolbar

import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.search_toolbar.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import com.artefaktur.kmp3.database.Track
import com.artefaktur.kmp3.intdb.IntDatabase


class MainActivity : AppCompatActivity(),
  PlayerStatusReceiver {

  companion object {
    lateinit var INSTANCE: MainActivity
  }


  lateinit var mediaPlayerHolder: MediaPlayerHolder
  lateinit var playerFragment: PlayerFragment
  lateinit var homeFragment: HomeFragment
  var lastFragment: BaseFragment? = null
  lateinit var menu: Menu
  var searchOpen: Boolean = false
  lateinit var intDb: IntDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    supportFragmentManager.transaction {
      homeFragment = HomeFragment.newInstance(this@MainActivity)
      playerFragment = PlayerFragment.newInstance(this@MainActivity)
      replace(R.id.main_replacement, homeFragment)
      replace(R.id.player_replacement, playerFragment)
    }
    createToolbar()
    INSTANCE = this
    createDb()
  }

  private fun createDb() {
    intDb = Room.databaseBuilder(
      applicationContext, IntDatabase::class.java, "kmp3"
    ).allowMainThreadQueries().build()
    intDb.initData()
  }

  private fun createToolbar() {
    val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
    myToolbar.setTitle("")
    setSupportActionBar(myToolbar)
    myToolbar.toolbar_home_button.setOnClickListener {
      resetSearch()

      supportFragmentManager.transaction {
        replace(R.id.main_replacement, homeFragment)
      }
    }
    myToolbar.toolbar_back_button.setOnClickListener {
      if (searchOpen) {
        resetSearch()
      } else {

        onBackPressed()
      }
    }
    myToolbar.toolbar_search_button.setOnClickListener {
      if (searchOpen == true) {
        resetSearch()
      } else {
        search_toolbar.visibility = View.VISIBLE
        search_bar_text.requestFocus()
        searchOpen = true
      }
    }
    search_toolbar.visibility = View.GONE

    search_bar_text.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable) {}
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.length == 0) {
          return
        }
        val lf = lastFragment
        if (lf != null) {
          lf.onSearch(s.toString())
        } else {
          toast("Keine Suche unterstützt")
        }
      }
    });

    search_bar_button.setOnClickListener {
      search_toolbar.visibility = View.GONE
    }

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    this.menu = menu
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.bar_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle item selection
    val handled = when (item.itemId) {
      R.id.menu_configure -> {
        val newFragment = SettingsDialog()
        goMainLink(newFragment)
        true
      }
      else -> false
    }
    if (handled == true) {
      return true
    }
    lastFragment?.let {
      if (it is BaseFragment) {
        if (it.handleMenuItem(this, item) == true) {
          return true
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  fun resetSearch() {
    if (searchOpen == false) {
      return
    }
    search_bar_text.setText("", TextView.BufferType.EDITABLE)
    lastFragment?.onResetSearch()
    searchOpen = false
    search_toolbar.visibility = View.GONE
  }

  fun onMediaPlayerCreated(mediaPlayerHolder: MediaPlayerHolder) {
    this.mediaPlayerHolder = mediaPlayerHolder
    playerFragment.initWithMediaPlayer(mediaPlayerHolder)
  }

  override fun onStartPlayTrack(track: Track) {
    lastFragment.let {
      if (it is PlayerStatusReceiver) {
        it.onStartPlayTrack(track)
      }
    }
  }

  override fun onStopPlayTrack(track: Track) {
    lastFragment.let {
      if (it is PlayerStatusReceiver) {
        it.onStopPlayTrack(track)
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
    if (requestCode == SettingsDialog.WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      resultData?.data?.also { uri ->
        Log.i("", "Uri: $uri")
        //showImage(uri)
      }

    }
  }
  fun getSettings(): AppSettings {
    val packageName = "com.artefaktur.kmp3"
    return AppSettings(getSharedPreferences(packageName, Context.MODE_PRIVATE))
  }

}

public fun getMainActivity(): MainActivity {
  return MainActivity.INSTANCE
}

public fun getMediaPlayerHolder(): MediaPlayerHolder {
  return MainActivity.INSTANCE.mediaPlayerHolder
}