package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.databinding.ItemCardviewGituserBinding
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.FavoriteGituser
import com.example.githubuser.view.detail.DetailActivity

class FavoriteAdapter(
    private val favoriteGituser: List<FavoriteGituser>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCardviewGituserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteGituser[position])
    }

    override fun getItemCount(): Int {
        return favoriteGituser.size
    }

    inner class ViewHolder(private val binding: ItemCardviewGituserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gituser: FavoriteGituser) {
            with(binding) {
                Glide.with(root.context)
                    .load(gituser.avatarUrl)
                    .apply(RequestOptions().override(150, 220))
                    .into(imgItemPhoto)
                tvItemName.text = gituser.login
                tvItemTypeName.text = gituser.type

                root.setOnClickListener {
                    val favoriteList = MappingHelper.convertToListGituser(gituser)
                    val moveToDetail = Intent(root.context, DetailActivity::class.java)
                    moveToDetail.putExtra(DetailActivity.EXTRA_DATA, favoriteList)
                    root.context.startActivity(moveToDetail)
                }
            }
        }
    }
}