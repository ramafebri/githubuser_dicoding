package com.example.consumerapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.GituserAdapter
import com.example.consumerapp.databinding.FragmentHomeBinding
import com.example.consumerapp.viewmodel.MainViewModel

class HomeFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        fragmentHomeBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(fragmentHomeBinding){
            rvGituserFragment.setHasFixedSize(true)
            rvGituserFragment.layoutManager = LinearLayoutManager(context)
        }
        initiateViewModel()
        initiateSearchView()
    }

    private fun initiateSearchView(){
        with(fragmentHomeBinding){
            searchView.queryHint = resources.getString(R.string.username_gitusers)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    progressBar.visibility = View.VISIBLE
                    mainViewModel.searchGituser(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText.isNullOrEmpty()){
                        mainViewModel.setGituser()
                    }
                    return false
                }
            })
        }
    }

    private fun initiateViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.setGituser()
        mainViewModel.getGituser().observe(viewLifecycleOwner, { gituserItems ->
            fragmentHomeBinding.progressBar.visibility = View.GONE
            if(gituserItems != null){
                val count = gituserItems.count()
                if(count==0){
                    Toast.makeText(context, getString(R.string.data_not_found), Toast.LENGTH_SHORT).show()
                }
                val cardViewGituserAdapter = GituserAdapter(gituserItems)
                fragmentHomeBinding.rvGituserFragment.adapter = cardViewGituserAdapter
            }
        })
    }
}