package com.artefaktur.kmp3

import android.content.Context
import com.artefaktur.kmp3.database.Mp3UsageDB
import java.io.File
import java.io.FileOutputStream

class Mp3UsageDb() {
  companion object {

    public var INSTANCE: Mp3UsageDb? = null
    @JvmStatic
    fun getInstance(): Mp3UsageDb {
      if (INSTANCE == null) {
        INSTANCE = Mp3UsageDb()
        INSTANCE!!.init()
      }
      return INSTANCE!!
    }
  }

  val db: Mp3UsageDB

  init {
    db = Mp3UsageDB(getInternFile(getMainActivity()))
  }

  fun get() {

  }

  private fun getInternFile(context: Context) = File(context.filesDir, "mp3usages.csv")

  private fun init() {
    val app = getMainActivity()
    val file = getInternFile(app)
    if (file.exists() == false || file.length() == 0L) {
      importFromExtern(app)
    }
    openDb()
  }

  private fun openDb() {

  }

  private fun importFromExtern(app: MainActivity) {
    val approot = app.getSettings().getMp3Root()
    val extFile = File(File(approot), "gwikiweb/acccexp/mp3usages.csv")
    if (extFile.exists() == false) {

      return
    }
    extFile.copyTo(getInternFile(app))
  }

}
