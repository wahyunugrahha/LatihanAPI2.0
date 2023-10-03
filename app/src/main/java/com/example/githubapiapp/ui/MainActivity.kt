package com.example.githubapiapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapiapp.R
import com.example.githubapiapp.databinding.ActivityMainBinding
import com.example.githubapiapp.ui.adapter.UserAdapter
import com.example.githubapiapp.ui.viewmodel.MainViewModel
import com.example.githubapiapp.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UserAdapter {
            val intent = Intent(this, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_LOGIN, it.login)
            startActivity(intent)
        }
        binding.rvAkun.layoutManager = LinearLayoutManager(this)
        binding.rvAkun.adapter = userAdapter

        mainViewModel.listUser.observe(this) { akun ->
            userAdapter.submitList(akun)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        mainViewModel.isEmpty.observe(this) {
            binding.tvEmpty.isVisible = it
        }
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        val searchItem = binding.topAppBar.menu.findItem(R.id.action_search)
        val settingItem = binding.topAppBar.menu.findItem(R.id.action_setting)
        val favoriteItem = binding.topAppBar.menu.findItem(R.id.action_favorite)

        favoriteItem.setOnMenuItemClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
            true
        }

        settingItem.setOnMenuItemClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            true
        }

        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                mainViewModel.getUser(s)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                mainViewModel.getUser(s)
                return false
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}