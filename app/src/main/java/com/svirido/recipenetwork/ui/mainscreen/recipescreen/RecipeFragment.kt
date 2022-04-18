package com.svirido.recipenetwork.ui.mainscreen.recipescreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.svirido.recipenetwork.MyApplication
import com.svirido.recipenetwork.databinding.FragmentRecipeBinding
import com.svirido.recipenetwork.ui.mainscreen.HomeViewModelFactory
import com.svirido.recipenetwork.ui.mainscreen.RecipeViewModel
import javax.inject.Inject

const val CALORIES = "Calories: "
const val TYPE_CUISINE = "Type cuisine: "
const val INGREDIENT = " Ingredient:"
const val DASH = "- "
const val TOTAL_TIME = "Total time: "
const val MINUTES = " minutes"
const val TOTAL_WEIGHT = "Total weight: "
const val GRAM = " gram"

class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding
    private lateinit var viewModel: RecipeViewModel
    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater)
        id = arguments?.getString("id_key") ?: ""
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MyApplication.appComponent.inject(this)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[RecipeViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            context?.let {
                Glide.with(it).load(recipe.recipe.images.SMALL.url)
                    .into(binding.imageRecipeImageView)
            }

            binding.sharringButton.setOnClickListener {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, viewModel.recipe.value?.recipe?.shareAs)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, null))

            }
            binding.backButton.setOnClickListener {
                requireActivity().onBackPressed()
            }

            binding.run {
                labelRecipeTextView.text = recipe.recipe.label
                cuisineTypeRecipeTextView.text = TYPE_CUISINE + recipe.recipe.cuisineType[0]
                caloriesRecipeTextView.text = CALORIES + recipe.recipe.calories.toInt()
                totalTimeRecipeTextView.text = TOTAL_TIME + recipe.recipe.totalTime.toInt() + MINUTES
                totalWeightRecipeTextView.text = TOTAL_WEIGHT + recipe.recipe.totalWeight.toInt() + GRAM
                ingredientesTextView.append(INGREDIENT)
            }

            var i = 0
            while (i < recipe.recipe.ingredientLines.size) {
                binding.ingredientesTextView.append("\n")
                binding.ingredientesTextView.append(DASH + recipe.recipe.ingredientLines[i])
                i++
            }
        }
        viewModel.getRecipe(id)
    }
}
