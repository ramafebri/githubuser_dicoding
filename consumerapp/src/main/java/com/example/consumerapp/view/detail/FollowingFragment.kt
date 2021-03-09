package com.example.consumerapp.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.GituserAdapter
import com.example.consumerapp.databinding.FragmentFollowingListBinding
import com.example.consumerapp.viewmodel.DetailViewModel

class FollowingFragment : Fragment() {
    private lateinit var fragmentFollowingListBinding: FragmentFollowingListBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFollowingListBinding.inflate(inflater, container, false)
        fragmentFollowingListBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(fragmentFollowingListBinding.rvFragmentFollowing) {
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
                detailViewModel.setFollowingGituser(gituserUsername)
            })
            detailViewModel.getFollowingGituser().observe(viewLifecycleOwner, { gituserFollowersItems ->
                fragmentFollowingListBinding.rvFragmentFollowing.adapter = GituserAdapter(gituserFollowersItems)
            })
        }
    }
}