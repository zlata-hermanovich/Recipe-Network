package com.svirido.recipenetwork.ui.mainscreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.svirido.recipenetwork.databinding.ItemRecipeBinding
import com.svirido.recipenetwork.model.Hit

class RecipeAdapter constructor(
    private val context: Context,
    val onItemClick: (item: Hit) -> Unit
) : RecyclerView.Adapter<RecipeViewHolder>() {

    private var list = listOf<Hit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(context))
        return RecipeViewHolder(binding, context).apply {
            setIsRecyclable(false)
        }
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
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