package com.example.consumerapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.helper.MappingHelper.convertToListGituser
import com.example.consumerapp.model.FavoriteGituser
import com.example.consumerapp.model.ListGituser
import com.example.consumerapp.view.detail.DetailActivity
import kotlinx.android.synthetic.main.item_cardview_followers.view.*

class FavoriteAdapter(
    private val favoriteGituser: List<FavoriteGituser>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardview_followers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteGituser[position])
    }

    override fun getItemCount(): Int {
        return favoriteGituser.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(gituser: FavoriteGituser) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(gituser.avatarUrl)
                    .apply(RequestOptions().override(150, 220))
                    .into(img_item_photo_followers)
                tv_item_name_followers.text = gituser.login
                tv_item_type_name_followers.text = gituser.type
            }
            itemView.setOnClickListener {
                val favoriteList = convertToListGituser(gituser)
                Log.i("MainActivity", favoriteList.toString())

                val moveToDetail = Intent(itemView.context, DetailActivity::class.java)
                moveToDetail.putExtra(DetailActivity.EXTRA_DATA, favoriteList)
                itemView.context.startActivity(moveToDetail)
            }
        }
    }
}