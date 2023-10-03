package com.example.githubapiapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapiapp.databinding.FragmentFollowBinding
import com.example.githubapiapp.ui.adapter.UserAdapter
import com.example.githubapiapp.ui.viewmodel.FollowViewModel
import com.example.githubapiapp.ui.viewmodel.ViewModelFactory

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var adapter: UserAdapter
    private val followViewModel: FollowViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = requireActivity().intent.getStringExtra(DetailUserActivity.EXTRA_LOGIN)

        val layoutManager = LinearLayoutManager(context)
        binding.rvGithub.layoutManager = layoutManager
        adapter = UserAdapter {
            val intent = Intent(requireActivity(), DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_LOGIN, it.login)
            startActivity(intent)
        }
        binding.rvGithub.adapter = adapter
        followViewModel.isEmpty.observe(viewLifecycleOwner) {
            binding.tvEmpty.isVisible = it
        }
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        if (index == 1) {
            followViewModel.getFollowers(username.toString())
            followViewModel.listFollowers.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        } else {
            followViewModel.getFollowings(username.toString())
            followViewModel.listFollowings.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }

}