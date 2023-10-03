package com.example.githubapiapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubapiapp.data.FavoriteRepository
import com.example.githubapiapp.data.Result
import com.example.githubapiapp.data.SettingPreferences
import com.example.githubapiapp.data.remote.response.ItemsItem

class MainViewModel(
    private val pref: SettingPreferences,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>?>()
    val listUser: LiveData<List<ItemsItem>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    init {
        getUser("wa")
    }

    fun getUser(username: String) {
        favoriteRepository.getUser(username).observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                    _isEmpty.value = false
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _isEmpty.value = false
                    _listUser.value = result.data
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

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}