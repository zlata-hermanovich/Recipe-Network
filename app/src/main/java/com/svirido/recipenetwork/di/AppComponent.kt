package com.svirido.recipenetwork.di

import com.svirido.recipenetwork.ui.favorite.FavoriteFragment
import com.svirido.recipenetwork.ui.favorite.adapter.FavoriteViewHolder
import com.svirido.recipenetwork.ui.mainscreen.MainFragment
import com.svirido.recipenetwork.ui.mainscreen.adapter.RecipeViewHolder
import com.svirido.recipenetwork.ui.mainscreen.recipescreen.RecipeFragment
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class]
)
@Singleton
interface AppComponent {

    fun inject(fragment: MainFragment)

    fun inject(fragment: FavoriteFragment)

    fun inject(viewHolder: RecipeViewHolder)

    fun inject(viewHolder: FavoriteViewHolder)

    fun inject(fragment:RecipeFragment)
}