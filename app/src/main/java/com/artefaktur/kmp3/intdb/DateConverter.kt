package com.artefaktur.kmp3.intdb

import android.arch.persistence.room.TypeConverter
import java.text.SimpleDateFormat

import java.util.*


class DateConverter {

  @TypeConverter
  fun toDate(dateLong: Long?): Date? {
    return if (dateLong == null) null else Date(dateLong)
  }

  @TypeConverter
  fun fromDate(date: Date?): Long? {
    return (if (date == null) null else date!!.getTime())?.toLong()
  }
}

fun Date?.toIsoString(): String {
  if (this == null) {
    return ""
  }
  val formatter = SimpleDateFormat("yyyy-MM-dd")
  return formatter.format(this)
}
