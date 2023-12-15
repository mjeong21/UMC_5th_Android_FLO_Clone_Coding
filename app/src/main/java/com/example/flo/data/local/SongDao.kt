package com.example.flo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flo.data.entities.Song

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable") // SongTable의 모든 값을 가져옴
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id") // SongTable에 매개변수로 넘겨준 id인 데이터에 isLike값을 업데이트 해줌
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike= :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumId")
    fun getSongsInAlbum(albumId: Int): List<Song>
}