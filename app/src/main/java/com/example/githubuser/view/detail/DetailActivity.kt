package com.example.githubuser.view.detail

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.R
import com.example.githubuser.adapter.DetailPagerAdapter
import com.example.githubuser.database.DatabaseContract
import com.example.githubuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.DetailGituser
import com.example.githubuser.model.FavoriteGituser
import com.example.githubuser.model.ListGituser
import com.example.githubuser.view.home.MainActivity
import com.example.githubuser.viewmodel.GituserViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var gituserViewModel: GituserViewModel
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
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = resources.getString(R.string.github_user_detail)
        supportActionBar?.elevation = 0f

        changeProgressBar(true)
        initiateViewPager()

        data = intent.getParcelableExtra(EXTRA_DATA)
        if (data != null) {
            data!!.login?.let { initiateViewModel(it) }
            data!!.login?.let { checkExistingData(it) }
        }
        btn_fav.setOnClickListener(this)
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
                btn_fav.setImageResource(R.drawable.ic_favorite_pink_24dp)
                isExist = true
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_fav) {
            if(isExist){
                uriWithId = Uri.parse("$CONTENT_URI/${favoriteGituser?.id}")
                val result = contentResolver.delete(uriWithId, null, null)

                if (result > 0) {
                    Toast.makeText(this@DetailActivity, R.string.success_delete, Toast.LENGTH_SHORT).show()
                    btn_fav.setImageResource(R.drawable.ic_favorite_black_24dp)
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
                            btn_fav.setImageResource(R.drawable.ic_favorite_pink_24dp)
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