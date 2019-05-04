package com.artefaktur.kmp3.intdb

import android.arch.persistence.room.*

@Dao
interface MediaDao {
  @Query("SELECT * FROM MediaDO")
  fun getAll(): List<MediaDO>

  @Query("SELECT * FROM MediaDO WHERE mediaPk  = :mediaPk")
  fun findByMediaId(mediaPk: String): MediaDO?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertMediaDOs(vararg medias: MediaDO)

  @Update
  fun updateMediaDOs(vararg medias: MediaDO)

  @Query("select count(*) from MediaDO")
  fun count(): Int

  @Query("DELETE FROM MediaDO")
  fun nukeTable()

}
