package com.artefaktur.kmp3

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.artefaktur.ourmp3.player.MediaPlayerHolder

import kotlinx.android.synthetic.main.activity_main.toolbar

import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.search_toolbar.*
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView


class MainActivity : AppCompatActivity(),
    PlayerFragment.OnFragmentInteractionListener {

    companion object {
        lateinit var INSTANCE: MainActivity
    }


    lateinit var mediaPlayerHolder: MediaPlayerHolder
    lateinit var playerFragment: PlayerFragment
    lateinit var homeFragment: HomeFragment
    var lastFragment: BaseFragment? = null

    var searchOpen: Boolean = false
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
    }

    fun createToolbar() {
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


    override fun onFragmentInteraction(uri: Uri) {
        Log.i("", "onListFragmentInteraction")
    }

}

public fun getMainActivity(): MainActivity {
    return MainActivity.INSTANCE
}

public fun getMediaPlayerHolder(): MediaPlayerHolder {
    return MainActivity.INSTANCE.mediaPlayerHolder
}