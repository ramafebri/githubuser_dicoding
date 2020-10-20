package com.example.consumerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.model.ListGituser
import com.example.consumerapp.view.detail.DetailActivity
import kotlinx.android.synthetic.main.item_cardview_gituser.view.*

class GituserAdapter (private val listGitUser: List<ListGituser>) : RecyclerView.Adapter<GituserAdapter.CardViewViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_gituser, parent, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listGitUser[position])
    }

    override fun getItemCount(): Int = listGitUser.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(gituser: ListGituser) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(gituser.avatarUrl)
                    .apply(RequestOptions().override(150, 220))
                    .into(img_item_photo)
                tv_item_name.text = gituser.login
                tv_item_type_name.text = gituser.type
                itemView.setOnClickListener {
                    val moveToDetail = Intent(itemView.context, DetailActivity::class.java)
                    moveToDetail.putExtra(DetailActivity.EXTRA_DATA, gituser)
                    itemView.context.startActivity(moveToDetail)
                }
            }
        }
    }
}