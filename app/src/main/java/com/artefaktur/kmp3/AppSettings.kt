package com.artefaktur.kmp3

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class AppSettings(val preferences: SharedPreferences) {
  companion object {
    const val FILTER_UNHEARED = "media.filter.unheared"
    const val FILTER_STARS = "media.filter.stars"
  }

  var onlyUnhearedMedia: Boolean
    get() = preferences.getBoolean(FILTER_UNHEARED, false)
    set(value) = preferences.edit().putBoolean(FILTER_UNHEARED, value).apply()
  var mediaStars: Int
    get() = preferences.getInt(FILTER_STARS, -1)
    set(value) = preferences.edit().putInt(FILTER_STARS, value).apply()

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