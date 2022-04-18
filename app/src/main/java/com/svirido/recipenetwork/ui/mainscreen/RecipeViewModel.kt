package com.svirido.recipenetwork.ui.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svirido.recipenetwork.model.Hit
import com.svirido.recipenetwork.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel (private val repository: Repository) : ViewModel() {

    val list = MutableLiveData<ArrayList<Hit>>()
    val recipe = MutableLiveData<Hit>()
    lateinit var showProgress: (isShow: Boolean) -> Unit

    fun getRecipeList() {
        viewModelScope.launch(Dispatchers.IO) {
            showProgress(true)
            val response = repository.getRecipe()
            if (response.isSuccessful) {
                list.postValue(response.body()?.hits as ArrayList<Hit>?)
            }
            showProgress(false)
        }
    }

    fun getRecipe(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getRecipeById(id)
            if (response.isSuccessful) {
                recipe.postValue(response.body())
            }
        }
    }
}