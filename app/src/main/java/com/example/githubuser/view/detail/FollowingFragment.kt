package com.example.githubuser.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.adapter.FollowingAdapter
import com.example.githubuser.viewmodel.GituserViewModel

class FollowingFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private lateinit var gituserViewModel: GituserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_fragment_following)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        initiateViewModel()
    }

    private fun initiateViewModel() {
        gituserViewModel = ViewModelProviders.of(requireActivity()).get(
            GituserViewModel::class.java
        )
        gituserViewModel.getUsernameGituser().observe(viewLifecycleOwner, { gituserUsername ->
            gituserViewModel.setFollowingGituser(gituserUsername)
        })
        gituserViewModel.getFollowingGituser().observe(viewLifecycleOwner, { gituserFollowersItems ->
            recyclerView?.adapter = FollowingAdapter(gituserFollowersItems)
        })
    }
}