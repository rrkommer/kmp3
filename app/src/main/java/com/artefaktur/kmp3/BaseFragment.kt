package com.artefaktur.kmp3

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

open class BaseFragment : Fragment() {
    var main: MainActivity? = null

    fun manager(): FragmentManager {
        return main!!.supportFragmentManager
    }


}


