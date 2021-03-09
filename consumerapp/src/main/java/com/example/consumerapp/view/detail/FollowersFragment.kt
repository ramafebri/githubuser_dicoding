package com.example.consumerapp.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.GituserAdapter
import com.example.consumerapp.databinding.FragmentFollowersListBinding
import com.example.consumerapp.viewmodel.DetailViewModel

class FollowersFragment : Fragment() {
    private lateinit var fragmentFollowersListBinding: FragmentFollowersListBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFollowersListBinding.inflate(inflater, container, false)
        fragmentFollowersListBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(fragmentFollowersListBinding.rvFragmentFollowers) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        initiateViewModel()
    }

    private fun initiateViewModel() {
        if (activity != null) {
            detailViewModel = ViewModelProviders.of(requireActivity()).get(
                DetailViewModel::class.java
            )
            detailViewModel.getUsernameGituser().observe(viewLifecycleOwner, { gituserUsername ->
                detailViewModel.setFollowersGituser(gituserUsername)
            })
            detailViewModel.getFollowersGituser().observe(viewLifecycleOwner, { gituserFollowersItems ->
                fragmentFollowersListBinding.rvFragmentFollowers.adapter = GituserAdapter(gituserFollowersItems)
            })
        }
    }
}