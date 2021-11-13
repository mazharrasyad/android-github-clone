package com.muhazharrasyad.aplikasigithubuserketiga.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.ItemGithubBinding
import com.muhazharrasyad.aplikasigithubuserketiga.model.Github

class GithubAdapter(private var listGithub: ArrayList<Github>) : RecyclerView.Adapter<GithubAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listGithub[position])
    }

    override fun getItemCount(): Int = listGithub.size

    inner class ListViewHolder(private val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(github: Github) {
            with(binding){
                Glide.with(itemView.context)
                    .load(github.avatar)
                    .into(imgAvatar)
                tvUsername.text = github.username

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(github) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Github)
    }

    // Favorite
    fun setListFavorite(listGithub: List<Github>){
        this.listGithub = listGithub as ArrayList<Github>
        this.notifyDataSetChanged()
    }
}