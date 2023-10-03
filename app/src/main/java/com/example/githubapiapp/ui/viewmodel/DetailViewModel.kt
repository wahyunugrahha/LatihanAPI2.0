package com.example.githubapiapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapiapp.data.FavoriteRepository
import com.example.githubapiapp.data.local.entity.FavoriteEntity
import com.example.githubapiapp.data.remote.response.GithubResponseDetail
import kotlinx.coroutines.launch
import com.example.githubapiapp.data.Result

class DetailViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    private val _userEntity = MutableLiveData<FavoriteEntity>()
    val userEntity: LiveData<FavoriteEntity> = _userEntity

    private val _detailUser = MutableLiveData<GithubResponseDetail?>()
    val detailUser: LiveData<GithubResponseDetail?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun addFavorite(user: FavoriteEntity) {
        viewModelScope.launch {
            favoriteRepository.addFavorite(user)
        }
    }

    fun deleteFavorite(username: String) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(username)
        }
    }

    fun getDetailUser(username: String) {
        favoriteRepository.getDetailUser(username).observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _detailUser.value = result.data
                    _userEntity.value = FavoriteEntity(0, result.data.login, result.data.avatarUrl)

                }

                is Result.Error -> {
                    _isLoading.value = false
                }

                is Result.Empty -> {
                    _isLoading.value = false
                }
            }
        }
    }

    fun isFavorite(username: String) = favoriteRepository.isFavorite(username)
}