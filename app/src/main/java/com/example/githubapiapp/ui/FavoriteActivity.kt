package com.example.githubapiapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapiapp.databinding.ActivityFavoriteBinding
import com.example.githubapiapp.ui.adapter.FavoriteAdapter
import com.example.githubapiapp.ui.viewmodel.FavoriteViewModel
import com.example.githubapiapp.ui.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        adapter = FavoriteAdapter({
            val intent = Intent(this, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_LOGIN, it.username)
            startActivity(intent)
        }, {
            favoriteViewModel.deleteFavorite(it.username.toString())
            favoriteViewModel.getAllFavorite().observe(this) { result ->
                adapter.submitList(result)
            }
        })
        binding.rvGithub.adapter = adapter
        favoriteViewModel.getAllFavorite().observe(this) { result ->
            binding.progressBar.isGone = true
            adapter.submitList(result)
            if (result.isEmpty()) {
                binding.tvEmpty.isVisible = true
            }
        }
    }
}