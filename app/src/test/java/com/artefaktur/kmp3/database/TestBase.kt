package com.artefaktur.kmp3.database

import java.io.File

open class TestBase {
  val baseDir = File("D:/ourMP3")
  val dbdir = File(baseDir, "gwikiweb/acccexp")
  val mproot = File(baseDir, "mp3root/classic")
  fun loadDb(): Mp3Db {
    return Mp3Db.get(dbdir.absolutePath, mproot.absolutePath)
  }
}