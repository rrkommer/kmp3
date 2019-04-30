package com.artefaktur.kmp3

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class AppSettings(val preferences: SharedPreferences) {

  fun getMp3Root(): String {
    val ret = preferences.getString("mp3root", "/storage/3633-6130/ourMP3/")
    if (ret.isNotBlank()) {
      return ret
    }
    return "/storage/3633-6130/ourMP3/"
  }

  fun storeMp3Root(rootDir: String) {
    preferences.edit().putString("mp3root", rootDir).apply()
  }
}