package com.example.githubapiapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.githubapiapp.R
import com.example.githubapiapp.data.local.entity.FavoriteEntity
import com.example.githubapiapp.databinding.ActivityDetailUserBinding
import com.example.githubapiapp.ui.adapter.SectionsPagerAdapter
import com.example.githubapiapp.ui.viewmodel.DetailViewModel
import com.example.githubapiapp.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private var user: FavoriteEntity = FavoriteEntity(0, null, null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        val username = intent.getStringExtra(EXTRA_LOGIN)
        detailViewModel.getDetailUser(username.toString())
        detailViewModel.userEntity.observe(this) {
            user = it
        }
        detailViewModel.detailUser.observe(this) {
            with(binding) {
                if (it != null) {
                    tvTotalFollowers.text = it.followers.toString()
                    tvTotalFollowing.text = it.following.toString()
                    tvUsername.text = it.login
                    tvFullname.text = it.name
                    tvTotalCommit.text = it.publicRepos.toString()
                    Glide.with(binding.root)
                        .load(it.avatarUrl)
                        .into(binding.ivAvatar)
                        .clearOnDetach()
                }
            }
        }
        detailViewModel.isLoading.observe(this) {
            binding.progressBar.isVisible = it

        }
        detailViewModel.isFavorite(username.toString()).observe(this) {
            if (it) {
                binding.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.btnFavorite.context,
                        R.drawable.ic_favorite
                    )
                )
                binding.btnFavorite.setOnClickListener {
                    detailViewModel.deleteFavorite(username.toString())
                }
            } else {
                binding.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.btnFavorite.context,
                        R.drawable.baseline_star_border_24
                    )
                )
                binding.btnFavorite.setOnClickListener {
                    detailViewModel.addFavorite(user)
                }
            }
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        const val EXTRA_LOGIN = "login"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}

