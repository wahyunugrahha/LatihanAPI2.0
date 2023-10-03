package com.example.githubapiapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.githubapiapp.data.local.entity.FavoriteEntity
import com.example.githubapiapp.data.local.room.FavoriteDao
import com.example.githubapiapp.data.remote.response.GithubResponseDetail
import com.example.githubapiapp.data.remote.response.ItemsItem
import com.example.githubapiapp.data.remote.retrofit.ApiService

class FavoriteRepository(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) {
    fun getUser(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(username)
            val items = response.items
            emit(Result.Success(items))
        } catch (e: Exception) {
            Log.d("FavoriteRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowing(username)
            if (response.isEmpty()) emit(Result.Empty)
            else emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("FavoriteRepository", "getFollowing: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowers(username)
            if (response.isEmpty()) emit(Result.Empty)
            else emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("FavoriteRepository", "getFollowers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailUser(username: String): LiveData<Result<GithubResponseDetail>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("FavoriteRepository", "getDetailUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorite()
    }

    fun isFavorite(username: String): LiveData<Boolean> {
        return favoriteDao.isFavorite(username)
    }

    suspend fun addFavorite(user: FavoriteEntity) {
        favoriteDao.addFavorite(user)
    }

    suspend fun deleteFavorite(username: String) {
        favoriteDao.deleteFavorite(username)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(apiService, favoriteDao)
            }.also { instance = it }
    }
}

