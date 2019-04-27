package com.artefaktur.kmp3

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction


public fun FragmentManager.transaction(callback: FragmentTransaction.() -> Unit): Unit {
    val trans = beginTransaction()
    trans.callback()
    trans.commit()
}