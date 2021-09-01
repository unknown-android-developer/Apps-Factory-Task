package com.test.appsfactorytask.core.api.db.room

import androidx.room.*
import com.test.appsfactorytask.core.api.db.data.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album")
    fun getAllAlbums(): Flow<List<Album>>

    @Query("SELECT * FROM album WHERE authorId = :authorId")
    suspend fun getAlbumsByAuthor(authorId: String): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album): Long

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("DELETE FROM album WHERE id = :albumId")
    suspend fun deleteAlbumById(albumId: Int)
}