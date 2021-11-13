package com.muhazharrasyad.aplikasigithubuserketiga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.ItemGithubBinding
import com.muhazharrasyad.aplikasigithubuserketiga.model.Follower

class FollowerAdapter(private val listFollower: ArrayList<Follower>) : RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFollower[position])
    }

    override fun getItemCount(): Int = listFollower.size

    inner class ListViewHolder(private val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(follower: Follower) {
            with(binding){
                Glide.with(itemView.context)
                    .load(follower.avatar)
                    .into(imgAvatar)
                tvUsername.text = follower.username
            }
        }
    }
}