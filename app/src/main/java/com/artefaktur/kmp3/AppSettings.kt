package com.artefaktur.kmp3

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class AppSettings(val preferences: SharedPreferences) {
  companion object {
    const val FILTER_UNHEARED = "media.filter.unheared"
  }

  var onlyUnhearedMedia: Boolean
    get() = preferences.getBoolean(FILTER_UNHEARED, false)
    set(value) = preferences.edit().putBoolean(FILTER_UNHEARED, value).apply()

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