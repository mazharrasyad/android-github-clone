package com.muhazharrasyad.aplikasigithubuserketiga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.ItemGithubBinding
import com.muhazharrasyad.aplikasigithubuserketiga.model.Following

class FollowingAdapter(private val listFollowing: ArrayList<Following>) : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollowing[position])
    }

    override fun getItemCount(): Int = listFollowing.size

    inner class ListViewHolder(private val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(following: Following) {
            with(binding){
                Glide.with(itemView.context)
                    .load(following.avatar)
                    .into(imgAvatar)
                tvUsername.text = following.username
            }
        }
    }
}