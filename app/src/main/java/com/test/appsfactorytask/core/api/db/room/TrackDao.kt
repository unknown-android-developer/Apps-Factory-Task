package com.test.appsfactorytask.core.api.db.room

import androidx.room.*
import com.test.appsfactorytask.core.api.db.data.Track

@Dao
interface TrackDao {

    @Query("SELECT * FROM track WHERE albumId = :albumId")
    fun getTracksByAlbum(albumId: Int): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTracks(tracks: List<Track>)

    @Query("DELETE FROM track WHERE albumId = :albumId")
    fun deleteTracksByAlbum(albumId: Int)
}