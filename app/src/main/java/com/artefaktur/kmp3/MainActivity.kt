package com.artefaktur.kmp3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.artefaktur.kmp3.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_list.*

import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
    TitleFragment.OnListFragmentInteractionListener,
    PlayerFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportFragmentManager.transaction {
            val homeFragment = HomeFragment.newInstance(this@MainActivity)
            val playerFragment = PlayerFragment.newInstance(this@MainActivity)
            replace(R.id.main_replacement, homeFragment)
            replace(R.id.player_replacement, playerFragment)
        }

    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.i("", "onListFragmentInteraction")
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.i("", "onListFragmentInteraction")
    }
}
