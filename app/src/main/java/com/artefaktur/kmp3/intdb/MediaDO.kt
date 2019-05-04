package com.artefaktur.kmp3.intdb

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(primaryKeys = arrayOf("mediaPk"))
data class MediaDO(
  @ColumnInfo(name = "mediaPk") val mediaPk: String,
  @ColumnInfo(name = "usage") var usage: Int = 0,
  @ColumnInfo(name = "lastHeared") var lastHeared: Date? = null,
  @ColumnInfo(name = "rating") var rating: Int = 0

) {
  fun ratingAsStars(): String {
    val sb = StringBuilder()
    for (i in 0 until rating) {
      sb.append('*')
    }
    return sb.toString()
  }
}
