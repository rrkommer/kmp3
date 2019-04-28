package com.artefaktur.kmp3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Title
import com.artefaktur.kmp3.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_list.*

import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
    TitleFragment.OnListFragmentInteractionListener,
    PlayerFragment.OnFragmentInteractionListener,
    TitleDetail.OnFragmentInteractionListener,
    ComposerListFragment.OnListFragmentInteractionListener {
    companion object {
        lateinit var INSTANCE: MainActivity
    }

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
        INSTANCE = this
    }

    override fun onSelectedTitleInList(item: Title?) {
        Log.i("", "onSelectedTitleInList")
        val title = TitleDetail.newInstance("", "")
        supportFragmentManager.transaction {
            replace(R.id.main_replacement, title)
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.i("", "onListFragmentInteraction")
    }


    override fun onSelectComposer(item: Composer?) {
        Log.i("", "onSelectComposer")
        if (item == null) {
            return
        }
        val compilerFragment = ComposerDetailFragment.newInstance(item)
        supportFragmentManager.transaction {
            replace(R.id.main_replacement, compilerFragment)
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
    }

}

public fun getMainActivity(): MainActivity {
    return MainActivity.INSTANCE
}