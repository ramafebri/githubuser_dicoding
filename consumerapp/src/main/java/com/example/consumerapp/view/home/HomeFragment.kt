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
import androidx.recyclerview.widget.RecyclerView
import com.example.consumerapp.R
import com.example.consumerapp.adapter.GituserAdapter
import com.example.consumerapp.viewmodel.GituserViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private lateinit var gituserViewModel: GituserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_gituser_fragment)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        initiateViewModel()
        initiateSearchView()
    }

    private fun initiateSearchView(){
        searchView.queryHint = resources.getString(R.string.username_gitusers)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                progressBar.visibility = View.VISIBLE
                gituserViewModel.searchGituser(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    gituserViewModel.setGituser()
                }
                return false
            }
        })
    }

    private fun initiateViewModel() {
        gituserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GituserViewModel::class.java)
        gituserViewModel.setGituser()
        gituserViewModel.getGituser().observe(viewLifecycleOwner, { gituserItems ->
            progressBar.visibility = View.GONE
            if(gituserItems != null){
                val count = gituserItems.count()
                if(count==0){
                    Toast.makeText(context, resources.getString(R.string.data_not_found), Toast.LENGTH_SHORT).show()
                }
                val cardViewGituserAdapter = GituserAdapter(gituserItems)
                recyclerView?.adapter = cardViewGituserAdapter
            }
        })
    }
}