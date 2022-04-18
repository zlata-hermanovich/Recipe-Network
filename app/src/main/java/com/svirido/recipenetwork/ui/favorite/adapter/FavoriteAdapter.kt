package com.svirido.recipenetwork.ui.favorite.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.svirido.recipenetwork.databinding.ItemRecipeFavoriteBinding
import com.svirido.recipenetwork.model.Hit
import javax.inject.Inject

class FavoriteAdapter @Inject constructor(
    private val context: Context,
    val onItemClick: (item: Hit) -> Unit
) :
    RecyclerView.Adapter<FavoriteViewHolder>() {

    private var list = listOf<Hit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRecipeFavoriteBinding.inflate(LayoutInflater.from(context))
        return FavoriteViewHolder(binding, context).apply {
            setIsRecyclable(false)
        }
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            onItemClick(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(recipe: List<Hit>) {
        list = recipe
        notifyDataSetChanged()
    }
}
