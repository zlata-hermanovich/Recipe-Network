package com.svirido.recipenetwork.ui.mainscreen.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.svirido.recipenetwork.MyApplication
import com.svirido.recipenetwork.R
import com.svirido.recipenetwork.databinding.ItemRecipeBinding
import com.svirido.recipenetwork.model.Hit
import com.svirido.recipenetwork.repository.PreferencesRepository
import javax.inject.Inject

const val CUISINE_TYPE = "Cuisine type: "
const val DISH_TYPE = "Dish type: "

class RecipeViewHolder(
    private val binding: ItemRecipeBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    @Inject
    lateinit var sharedPreferences: PreferencesRepository

    private var list: ArrayList<Hit> = arrayListOf()
    private var isOnFavorite = false
    private lateinit var auth: FirebaseAuth

    fun bind(recipe: Hit) {

        MyApplication.appComponent.inject(this)

        binding.labelTextView.text = recipe.recipe.label
        binding.cuisineTypeTextView.text = CUISINE_TYPE + recipe.recipe.cuisineType[0]
        binding.dishTypeTextView.text = DISH_TYPE + recipe.recipe.dishType[0]

        auth = FirebaseAuth.getInstance()

        Glide.with(context).load(recipe.recipe.images.SMALL.url).into(binding.recipeImage)

        list = if (auth.currentUser != null) {
            sharedPreferences.favoriteJsonToArrayAuthUser(auth.currentUser?.uid ?: "") ?: arrayListOf()
        } else {
            sharedPreferences.favoriteJsonToArray() ?: arrayListOf()
        }
        isOnFavorite = list.find { it._links.self.href == recipe._links.self.href } != null

        if (isOnFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.ic_not_favorite)
        }
        binding.favoriteButton.setOnClickListener {
            if (isOnFavorite) {
                binding.favoriteButton.setImageResource(R.drawable.ic_not_favorite)
                if (auth.currentUser != null) {
                    sharedPreferences.deleteRecipeAuthUser(recipe, auth.currentUser?.uid ?: "")
                } else {
                    sharedPreferences.deleteRecipe(recipe)
                }
            } else {
                binding.favoriteButton.setImageResource(R.drawable.ic_favorite)

                if (auth.currentUser != null) {
                    sharedPreferences.saveRecipeAuthUser(recipe, auth.currentUser?.uid ?: "")
                } else {
                    sharedPreferences.saveRecipe(recipe)
                }
            }
            isOnFavorite = !isOnFavorite
        }
    }
}
