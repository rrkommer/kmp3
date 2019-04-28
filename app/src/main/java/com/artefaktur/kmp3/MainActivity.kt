package com.artefaktur.kmp3

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Title
import com.artefaktur.ourmp3.player.MediaPlayerHolder

import kotlinx.android.synthetic.main.activity_main.toolbar

import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity(),
    PlayerFragment.OnFragmentInteractionListener {

    companion object {
        lateinit var INSTANCE: MainActivity
    }


    lateinit var mediaPlayerHolder: MediaPlayerHolder
    lateinit var playerFragment: PlayerFragment
    lateinit var homeFragment: HomeFragment

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
            supportFragmentManager.transaction {
                replace(R.id.main_replacement, homeFragment)
            }
        }
//        myToolbar.setNavigationIcon(R.drawable.ic_drawer)
//        myToolbar.setLogo(R.drawable.brandz)
//        myToolbar.setTitle("KMP3")
//        myToolbar.setSubtitle("MyToolbar")
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