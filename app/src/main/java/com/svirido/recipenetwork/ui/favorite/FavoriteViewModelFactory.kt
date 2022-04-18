package com.svirido.recipenetwork.ui.favorite

import com.svirido.recipenetwork.repository.PreferencesRepository
import com.svirido.recipenetwork.ui.base.BaseViewModelFactory
import javax.inject.Inject

class FavoriteViewModelFactory @Inject constructor(
    private val sharedPreferences: PreferencesRepository
) : BaseViewModelFactory<FavoriteViewModel>(FavoriteViewModel::class.java) {

    override fun createViewModel(): FavoriteViewModel {
        return FavoriteViewModel(sharedPreferences)
    }
}