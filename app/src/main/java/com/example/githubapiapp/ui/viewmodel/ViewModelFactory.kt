package com.example.githubapiapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapiapp.data.FavoriteRepository
import com.example.githubapiapp.data.SettingPreferences
import com.example.githubapiapp.data.dataStore
import com.example.githubapiapp.di.Injection

class ViewModelFactory private constructor(
    private val pref: SettingPreferences,
    private val favoriteRepository: FavoriteRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MainViewModel::class.java -> MainViewModel(pref, favoriteRepository)
        FollowViewModel::class.java -> FollowViewModel(favoriteRepository)
        DetailViewModel::class.java -> DetailViewModel(favoriteRepository)
        SettingViewModel::class.java -> SettingViewModel(pref)
        FavoriteViewModel::class.java -> FavoriteViewModel(favoriteRepository)
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(application: Application): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    SettingPreferences.getInstance(application.dataStore),
                    Injection.provideRepository(application.applicationContext)
                )
            }.also { instance = it }
    }
}