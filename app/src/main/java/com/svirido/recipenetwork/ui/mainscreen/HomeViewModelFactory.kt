package com.svirido.recipenetwork.ui.mainscreen

import com.svirido.recipenetwork.di.NetWorkManager
import com.svirido.recipenetwork.repository.Repository
import com.svirido.recipenetwork.ui.base.BaseViewModelFactory
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor() : BaseViewModelFactory<RecipeViewModel>(RecipeViewModel::class.java) {

    override fun createViewModel(): RecipeViewModel {
        return RecipeViewModel(
            Repository(NetWorkManager())
        )
    }
}