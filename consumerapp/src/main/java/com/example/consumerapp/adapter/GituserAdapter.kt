package com.example.consumerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.databinding.ItemCardviewGituserBinding
import com.example.consumerapp.model.ListGituser
import com.example.consumerapp.view.detail.DetailActivity

class GituserAdapter (private val listGitUser: List<ListGituser>) : RecyclerView.Adapter<GituserAdapter.CardViewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewViewHolder {
        val view = ItemCardviewGituserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listGitUser[position])
    }

    override fun getItemCount(): Int = listGitUser.size

    inner class CardViewViewHolder(private val binding: ItemCardviewGituserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gituser: ListGituser) {
            with(binding) {
                com.bumptech.glide.Glide.with(root.context)
                    .load(gituser.avatarUrl)
                    .apply(RequestOptions().override(150, 220))
                    .into(imgItemPhoto)
                tvItemName.text = gituser.login
                tvItemTypeName.text = gituser.type
                root.setOnClickListener {
                    val moveToDetail = Intent(root.context, DetailActivity::class.java)
                    moveToDetail.putExtra(DetailActivity.EXTRA_DATA, gituser)
                    root.context.startActivity(moveToDetail)
                }
            }
        }
    }
}