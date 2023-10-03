package com.example.githubapiapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapiapp.data.FavoriteRepository
import com.example.githubapiapp.data.Result
import com.example.githubapiapp.data.remote.response.ItemsItem

class FollowViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>?>()
    val listFollowers: LiveData<List<ItemsItem>?> = _listFollowers

    private val _listFollowings = MutableLiveData<List<ItemsItem>?>()
    val listFollowings: LiveData<List<ItemsItem>?> = _listFollowings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty
    fun getFollowers(username: String) {
        favoriteRepository.getFollowers(username).observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                    _isEmpty.value = false
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _isEmpty.value = false
                    _listFollowers.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    _isEmpty.value = false
                }

                is Result.Empty -> {
                    _isLoading.value = false
                    _isEmpty.value = true
                }
            }
        }
    }

    fun getFollowings(username: String) {
        favoriteRepository.getFollowing(username).observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                    _isEmpty.value = false
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _isEmpty.value = false
                    _listFollowings.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    _isEmpty.value = false
                }

                is Result.Empty -> {
                    _isLoading.value = false
                    _isEmpty.value = true
                }
            }
        }
    }
}