package com.example.githubuser.view.detail

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.adapter.DetailPagerAdapter
import com.example.githubuser.database.DatabaseContract
import com.example.githubuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.DetailGituser
import com.example.githubuser.model.FavoriteGituser
import com.example.githubuser.model.ListGituser
import com.example.githubuser.view.home.MainActivity
import com.example.githubuser.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var uriWithId: Uri
    private lateinit var uriWithUsername: Uri

    private var favoriteGituser: FavoriteGituser? = null
    private var data: ListGituser? = null
    private var isExist = false

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

        binding.btnFav.setOnClickListener(this)
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
                binding.btnFav.setImageResource(R.drawable.ic_favorite_pink_24dp)
                isExist = true
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_fav) {
            if(isExist){
                uriWithId = Uri.parse("$CONTENT_URI/${favoriteGituser?.id}")
                val result = contentResolver.delete(uriWithId, null, null)

                if (result > 0) {
                    Toast.makeText(this@DetailActivity, R.string.success_delete, Toast.LENGTH_SHORT).show()
                    binding.btnFav.setImageResource(R.drawable.ic_favorite_black_24dp)
                    isExist = false
                    val moveToHome = Intent(this@DetailActivity, MainActivity::class.java)
                    startActivity(moveToHome)
                } else {
                    Toast.makeText(this@DetailActivity, R.string.fail_delete, Toast.LENGTH_SHORT).show()
                }
            } else {
                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns.USERNAME, data?.login)
                values.put(DatabaseContract.FavoriteColumns.TYPE, data?.type)
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, data?.avatarUrl)

                val result = contentResolver.insert(CONTENT_URI, values)
                if (result != null) {
                    val resultSize = result.lastPathSegment?.toLong()
                    if (resultSize != null) {
                        if (resultSize > 0) {
                            Toast.makeText(this@DetailActivity, R.string.success_insert, Toast.LENGTH_SHORT).show()
                            binding.btnFav.setImageResource(R.drawable.ic_favorite_pink_24dp)
                            isExist = true
                            finish()
                        } else {
                            Toast.makeText(this@DetailActivity, R.string.fail_delete, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}