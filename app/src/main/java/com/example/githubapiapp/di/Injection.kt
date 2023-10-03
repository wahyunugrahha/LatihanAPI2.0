package com.example.githubapiapp.di


import android.content.Context
import com.example.githubapiapp.data.FavoriteRepository
import com.example.githubapiapp.data.local.room.FavoriteDatabase
import com.example.githubapiapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        return FavoriteRepository.getInstance(apiService, dao)
    }
}