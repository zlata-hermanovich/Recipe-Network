package com.svirido.recipenetwork.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.svirido.recipenetwork.model.Hit
import com.svirido.recipenetwork.repository.PreferencesRepository

class FavoriteViewModel(
    private val sharedPreferences: PreferencesRepository
    ) : ViewModel() {

    val list = MutableLiveData<ArrayList<Hit>>()

    fun getRecipeListFromFirebase(uid:String) {
        list.value = sharedPreferences.favoriteJsonToArrayAuthUser(uid)
    }

    fun getRecipeListFromPreferences() {
        list.value = sharedPreferences.favoriteJsonToArray()
    }
}