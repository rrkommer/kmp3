package com.artefaktur.kmp3

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class AppSettings(val preferences: SharedPreferences) {
  companion object {
    const val FILTER_UNHEARED = "media.filter.unheared"
    const val FILTER_STARS = "media.filter.stars"
    const val STATS_FILESIZE = "stats.filesize"
    const val STATS_TIME = "stats.time"
  }

  var onlyUnhearedMedia: Boolean
    get() = preferences.getBoolean(FILTER_UNHEARED, false)
    set(value) = preferences.edit().putBoolean(FILTER_UNHEARED, value).apply()
  var mediaStars: Int
    get() = preferences.getInt(FILTER_STARS, -1)
    set(value) = preferences.edit().putInt(FILTER_STARS, value).apply()

  var mp3FileSize: Long
    get() = preferences.getLong(STATS_FILESIZE, 0L)
    set(value) = preferences.edit().putLong(STATS_FILESIZE, value).apply()
  var mp3Time: Long
    get() = preferences.getLong(STATS_TIME, 0L)
    set(value) = preferences.edit().putLong(STATS_TIME, value).apply()

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