package com.svirido.recipenetwork.ui.mainscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.svirido.recipenetwork.MyApplication
import com.svirido.recipenetwork.R
import com.svirido.recipenetwork.databinding.FragmentMainBinding
import com.svirido.recipenetwork.model.Hit
import com.svirido.recipenetwork.repository.Search
import com.svirido.recipenetwork.ui.mainscreen.adapter.RecipeAdapter
import com.svirido.recipenetwork.ui.mainscreen.recipescreen.RecipeFragment
import java.net.URI
import javax.inject.Inject

const val ID_KEY = "id_key"
const val PREFIX = "/api/recipes/v2/"

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: RecipeViewModel
    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MyApplication.appComponent.inject(this)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[RecipeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showProgress = {
            requireActivity().runOnUiThread {
                binding.progressBar.visibility = if (it) {
                    View.VISIBLE
                } else View.GONE
            }
        }

        viewModel.list.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                setList(it)
            }
        }

        viewModel.getRecipeList()

        binding.searchEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                binding.spinnerCuisineType.visibility = View.GONE
                binding.spinnerDishType.visibility = View.GONE
                binding.spinnerMealType.visibility = View.GONE
                return@OnEditorActionListener true
            }
            false
        })

        binding.run {
            filtersButton.setOnClickListener {
                if (spinnerCuisineType.visibility == View.VISIBLE) {
                    spinnerCuisineType.visibility = View.GONE
                    spinnerDishType.visibility = View.GONE
                    spinnerMealType.visibility = View.GONE
                } else {
                    spinnerCuisineType.visibility = View.VISIBLE
                    spinnerDishType.visibility = View.VISIBLE
                    spinnerMealType.visibility = View.VISIBLE
                }
            }
        }

        // spinner Cuisine Type
        val adapterCuisineType =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Search.cuisineTypeArrayList
            )
        adapterCuisineType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCuisineType.adapter = adapterCuisineType
        binding.spinnerCuisineType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (position != 0) {
                        Search.cuisineType = Search.cuisineTypeArrayList[position]
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        // spinner Dish Type
        val adapterDishType =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Search.dishTypeArrayList
            )
        adapterDishType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDishType.adapter = adapterDishType
        binding.spinnerDishType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (position != 0) {
                        Search.dishType = Search.dishTypeArrayList[position]
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        // spinner Meal Type
        val adapterMealType =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Search.mealTypeArrayList
            )
        adapterMealType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMealType.adapter = adapterMealType
        binding.spinnerMealType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    if (position != 0) {
                        Search.mealType = Search.mealTypeArrayList[position]
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun search() {
        Search.search = binding.searchEditText.text.toString()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.containerMain, MainFragment())
            .commit()
    }

    private fun setList(list: List<Hit>) {
        binding.recyclerView.run {
            if (adapter == null) {
                adapter = RecipeAdapter(requireContext()) {

                    val uri = URI(it._links.self.href)
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.containerMain, RecipeFragment().apply {
                            arguments = Bundle().apply {
                                putString(ID_KEY, uri.path.removePrefix(PREFIX))
                            }
                        })
                        .addToBackStack("")
                        .commit()
                }
                layoutManager = GridLayoutManager(requireContext(), 1)
                val dividerItemDecoration = DividerItemDecoration(
                    context, LinearLayout.VERTICAL
                )
                dividerItemDecoration.setDrawable(getDrawable(context,R.drawable.margin_recipe_list)!!)
                this.addItemDecoration(dividerItemDecoration)
            }
            (adapter as RecipeAdapter).setDataList(list)
        }
    }
}