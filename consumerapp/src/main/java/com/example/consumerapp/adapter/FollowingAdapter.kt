package com.example.consumerapp.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.model.ListGituser
import kotlinx.android.synthetic.main.item_cardview_gituser.view.*

class FollowingAdapter(
    private val listGitUser: List<ListGituser>
) : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardview_gituser, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listGitUser[position])
    }

    override fun getItemCount(): Int = listGitUser.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(gituser: ListGituser) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(gituser.avatarUrl)
                    .apply(RequestOptions().override(150, 220))
                    .into(img_item_photo)
                tv_item_name.text = gituser.login
                tv_item_type_name.text = gituser.type
            }
        }
    }
}