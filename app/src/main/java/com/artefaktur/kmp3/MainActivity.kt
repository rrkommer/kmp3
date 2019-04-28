package com.artefaktur.kmp3

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Title

import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
    PlayerFragment.OnFragmentInteractionListener {
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



    override fun onFragmentInteraction(uri: Uri) {
        Log.i("", "onListFragmentInteraction")
    }

}
public fun getMainActivity(): MainActivity {
    return MainActivity.INSTANCE
}