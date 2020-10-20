package com.example.githubuser.view.home

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
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.adapter.FavoriteAdapter
import com.example.githubuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuser.database.FavoriteHelper
import com.example.githubuser.helper.MappingHelper
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var resolver: ContentResolver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_fragment_favorite)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

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

    private fun initiateDatabase(){
        favoriteHelper = context?.let { FavoriteHelper.getInstance(it) }!!
        favoriteHelper.open()
        getData()
    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            if (context != null){
                progressBarFavorite.visibility = View.VISIBLE
            }
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = resolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favoriteGituser = deferredFavorites.await()

            if (context != null){
                progressBarFavorite.visibility = View.GONE
            }

            if (favoriteGituser.size > 0) {
                recyclerView?.adapter = FavoriteAdapter(favoriteGituser)
            } else {
                if (context != null){
                    Toast.makeText(context, R.string.data_not_found, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}