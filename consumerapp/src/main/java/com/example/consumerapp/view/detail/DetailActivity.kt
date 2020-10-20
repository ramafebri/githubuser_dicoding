package com.example.consumerapp.view.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.adapter.DetailPagerAdapter
import com.example.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.helper.MappingHelper
import com.example.consumerapp.model.DetailGituser
import com.example.consumerapp.model.FavoriteGituser
import com.example.consumerapp.model.ListGituser
import com.example.consumerapp.viewmodel.GituserViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(){
    private lateinit var gituserViewModel: GituserViewModel
    private lateinit var uriWithUsername: Uri

    private var favoriteGituser: FavoriteGituser? = null
    private var data: ListGituser? = null

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = resources.getString(R.string.github_user_detail)
        supportActionBar?.elevation = 0f

        changeProgressBar(true)
        initiateViewPager()

        data = intent.getParcelableExtra(EXTRA_DATA)
//        Log.i("MainActivity", data.toString())
        if (data != null) {
            data!!.login?.let { initiateViewModel(it) }
            data!!.login?.let { checkExistingData(it) }
        }
    }

    private fun initiateViewPager(){
        val detailPagerAdapter = DetailPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = detailPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun initiateViewModel(username: String) {
        gituserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            GituserViewModel::class.java
        )
        gituserViewModel.getLoadingDetail().observe(this, { loading ->
            changeProgressBar(loading)
        })
        gituserViewModel.setUsernameGituser(username)
        gituserViewModel.setDetailGituser(username)
        gituserViewModel.getDetailGituser().observe(this, { gituserDetailItems ->
            putDataToView(gituserDetailItems)
        })
    }

    private fun checkExistingData(username: String){
        GlobalScope.launch(Dispatchers.Main) {
            uriWithUsername = Uri.parse("$CONTENT_URI/username")
            val cursor = contentResolver.query(uriWithUsername, null, username, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                favoriteGituser = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        }
    }

    private fun changeProgressBar(loading: Boolean){
        if(loading){
            progressBar_detail.visibility = View.VISIBLE
        }
        progressBar_detail.visibility = View.GONE
    }

    private fun putDataToView(data: DetailGituser){
        val tvName: TextView = findViewById(R.id.name_detail)
        val tvFollowers: TextView = findViewById(R.id.followers_detail)
        val tvFollowing: TextView = findViewById(R.id.following_detail)
        val tvUsername: TextView = findViewById(R.id.username_detail)
        val tvCompany: TextView = findViewById(R.id.company_detail)
        val tvLocation: TextView = findViewById(R.id.location_detail)
        val tvRepository: TextView = findViewById(R.id.repository_detail)
        val imgPhoto: ImageView = findViewById(R.id.avatar_detail)

        tvName.text = data.name
        tvFollowers.text = data.followers.toString()
        tvFollowing.text = data.following.toString()
        tvUsername.text = data.login
        tvCompany.text = data.company
        tvRepository.text = data.publicRepos.toString()
        tvLocation.text = data.location

        Glide.with(this)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(180, 180))
            .into(imgPhoto)
    }
}