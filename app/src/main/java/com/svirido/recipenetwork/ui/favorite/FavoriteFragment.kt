package com.svirido.recipenetwork.ui.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.svirido.recipenetwork.MyApplication
import com.svirido.recipenetwork.R
import com.svirido.recipenetwork.databinding.FragmentFavoriteBinding
import com.svirido.recipenetwork.model.Hit
import com.svirido.recipenetwork.ui.favorite.adapter.FavoriteAdapter
import com.svirido.recipenetwork.ui.mainscreen.ID_KEY
import com.svirido.recipenetwork.ui.mainscreen.PREFIX
import com.svirido.recipenetwork.ui.mainscreen.recipescreen.RecipeFragment
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.net.URI
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoriteBinding
    @Inject
    lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MyApplication.appComponent.inject(this)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[FavoriteViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()

        viewModel.list.observe(viewLifecycleOwner) { it ->
            binding.recyclerViewFavorite.run {
                adapter = FavoriteAdapter(requireContext()) { it ->
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
                dividerItemDecoration.setDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.margin_recipe_list
                    )!!
                )
                this.addItemDecoration(dividerItemDecoration)
            }
            (it as? ArrayList<Hit>)?.let { it ->
                (binding.recyclerViewFavorite.adapter as FavoriteAdapter).setDataList(it)
            }
        }

        if (auth.currentUser != null) {
            viewModel.getRecipeListFromFirebase(auth.currentUser?.uid?:"")
        } else {
            viewModel.getRecipeListFromPreferences()
        }
    }
}
