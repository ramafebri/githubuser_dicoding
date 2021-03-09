package com.example.consumerapp.view.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.adapter.DetailPagerAdapter
import com.example.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.databinding.ActivityDetailBinding
import com.example.consumerapp.helper.MappingHelper
import com.example.consumerapp.model.DetailGituser
import com.example.consumerapp.model.FavoriteGituser
import com.example.consumerapp.model.ListGituser
import com.example.consumerapp.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(){
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var uriWithUsername: Uri

    private var favoriteGituser: FavoriteGituser? = null
    private var data: ListGituser? = null

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.github_user_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        changeProgressBar(true)
        initiateViewPager()

        data = intent.getParcelableExtra(EXTRA_DATA)
        if (data != null) {
            data!!.login?.let { initiateViewModel(it) }
            data!!.login?.let { checkExistingData(it) }
        }
    }

    private fun initiateViewPager(){
        val detailPagerAdapter = DetailPagerAdapter(this, supportFragmentManager)
        with(binding){
            viewPager.adapter = detailPagerAdapter
            tabs.setupWithViewPager(binding.viewPager)
        }
    }

    private fun initiateViewModel(username: String) {
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )
        detailViewModel.getLoadingDetail().observe(this, { loading ->
            changeProgressBar(loading)
        })
        detailViewModel.setUsernameGituser(username)
        detailViewModel.setDetailGituser(username)
        detailViewModel.getDetailGituser().observe(this, { gituserDetailItems ->
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
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    private fun putDataToView(data: DetailGituser){
        with(binding){
            nameDetail.text = data.name
            followersDetail.text = data.followers.toString()
            followingDetail.text = data.following.toString()
            usernameDetail.text = data.login
            companyDetail.text = data.company
            repositoryDetail.text = data.publicRepos.toString()
            locationDetail.text = data.location
            Glide.with(root.context)
                .load(data.avatarUrl)
                .apply(RequestOptions().override(180, 180))
                .into(avatarDetail)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}