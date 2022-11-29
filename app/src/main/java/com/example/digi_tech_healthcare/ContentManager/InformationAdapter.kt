package com.example.digi_tech_healthcare.ContentManager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digi_tech_healthcare.Dataclasses.Articles
import com.example.digi_tech_healthcare.databinding.ItemContentBinding

class ContentAdapter(private val articleList: ArrayList<Articles>, private val listener: (Articles) -> Unit): RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContentViewHolder {
        val binding = ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = articleList[position]
        holder.title.text = item.title
        holder.desc.text = item.description
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }
    override fun getItemCount(): Int {
        return articleList.size
    }
    class ContentViewHolder(binding: ItemContentBinding): RecyclerView.ViewHolder(binding.root){
        val title = binding.tvContentTitle
        val desc = binding.tvContentDesc
    }


}