package com.artefaktur.kmp3

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Track


public fun FragmentManager.transaction(callback: FragmentTransaction.() -> Unit): Unit {
    val trans = beginTransaction()
    trans.callback()
    trans.commit()
}

public fun doTrans(callback: FragmentTransaction.() -> Unit) {
    getMainActivity().supportFragmentManager.transaction(callback)
}

public fun goMainLink(fragment: Fragment) {
    pushClient(fragment, R.id.main_replacement)
}

public fun pushClient(fragment: Fragment, id: Int) {
    doTrans {
        val ma = getMainActivity()
        ma.resetSearch()
        replace(id, fragment)
        addToBackStack(null)
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (fragment is BaseFragment) {
            ma.lastFragment = fragment
        } else {
            ma.lastFragment = null
        }
    }
}

public fun toast(text: String) {
    Toast.makeText(getMainActivity(), text, Toast.LENGTH_LONG).show()
}

public fun currentTrackPlaying(): Track? {
    return getMainActivity().mediaPlayerHolder.currentSong
}

public inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

object UIUtils {

    fun getColor(context: Context, color: Int, emergencyColor: Int): Int {
        return try {
            ContextCompat.getColor(context, color)
        } catch (e: Exception) {
            ContextCompat.getColor(context, emergencyColor)
        }
    }
//
//    fun setupSearch(
//        searchView: SearchView, artistsAdapter: ComposerAdapter,
//        artists: List<Composer>,
//        waveView: WaveView
//    ) {
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//
//            override
//            fun onQueryTextChange(newText: String): Boolean {
//                processQuery(newText, artistsAdapter, artists)
//                return false
//            }
//
//            override
//            fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//        })
//
//        searchView.setOnQueryTextFocusChangeListener { _: View, hasFocus: Boolean ->
//            waveView.visibility = if (hasFocus) View.GONE else View.VISIBLE
//            if (!hasFocus) searchView.isIconified = true
//        }
//    }
//
//    private fun processQuery(query: String, artistsAdapter: ComposerListRecyclerViewAdapter, artists: List<Composer>) {
//        // in real app you'd have it instantiated just once
//        val results = mutableListOf<Composer>()
//
//        try {
//            // case insensitive onSearch
//            artists.forEach {
//                if (it.name.toLowerCase().startsWith(query.toLowerCase())) {
//                    results.add(it)
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        if (results.size > 0) {
//            artistsAdapter.setQueryResults(results)
//        }
//    }

    fun setHorizontalScrollBehavior(parentView: View, vararg textViews: TextView) {
        var isLongPressed = false

        parentView.setOnLongClickListener {
            if (!isLongPressed) {
                textViews.forEachIndexed { _, textView ->
                    textView.isSelected = true
                }
                isLongPressed = true
            }
            return@setOnLongClickListener true
        }

        parentView.setOnTouchListener { _, e ->
            if (isLongPressed && e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_OUTSIDE || e.action == MotionEvent.ACTION_MOVE) {

                textViews.forEach {
                    it.isSelected = false
                }
                isLongPressed = false
            }
            return@setOnTouchListener false
        }
    }
}

