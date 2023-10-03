package com.example.githubapiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapiapp.data.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    fun getAllFavorite() = favoriteRepository.getAllFavorite()
    fun deleteFavorite(username: String) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(username)
        }
    }
}