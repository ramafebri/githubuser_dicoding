package com.example.consumerapp.view.home

import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.R
import com.example.consumerapp.adapter.FavoriteAdapter
import com.example.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.databinding.FragmentFavoriteBinding
import com.example.consumerapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var fragmentFavoriteBinding: FragmentFavoriteBinding
    private lateinit var resolver: ContentResolver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        fragmentFavoriteBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(fragmentFavoriteBinding.rvFragmentFavorite) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                getData()
            }
        }
        resolver = requireContext().contentResolver
        resolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            getData()
        }
    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            if (context != null){
                fragmentFavoriteBinding.progressBarFavorite.visibility = View.VISIBLE
            }
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = resolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteGituser = deferredFavorites.await()

            if (context != null){
                fragmentFavoriteBinding.progressBarFavorite.visibility = View.GONE
            }

            if (favoriteGituser.size > 0) {
                fragmentFavoriteBinding.rvFragmentFavorite.adapter = FavoriteAdapter(favoriteGituser)
            } else {
                if (context != null){
                    Toast.makeText(context, R.string.data_not_found, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}