package com.artefaktur.kmp3.database

import org.junit.Test

class TestReadMedia : TestBase() {

  @Test
  fun testLoad() {
    val db = loadDb()
    val composer = db.getComposerByName("Nyman, Michael")
    val start = System.currentTimeMillis()
    val mediend = db.getMediaByComposer(composer)
    System.out.println("Init ms ${System.currentTimeMillis() - start}")
    for (media in mediend) {
      System.out.println(media.name1)
    }
    val start2 = System.currentTimeMillis()
    val mediend2 = db.getMediaByComposer(composer)
    System.out.println("Init2 ms ${System.currentTimeMillis() - start2}")

  }
}