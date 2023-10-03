package com.example.githubapiapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.githubapiapp.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addFavorite(user: FavoriteEntity)

    @Query("DELETE from user where username= :username")
    suspend fun deleteFavorite(username: String)

    @Query("SELECT * from user order by id asc")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT * from user where username= :username)")
    fun isFavorite(username: String): LiveData<Boolean>
}