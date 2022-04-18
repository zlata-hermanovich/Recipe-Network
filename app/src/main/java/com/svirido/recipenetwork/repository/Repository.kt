package com.svirido.recipenetwork.repository

import com.svirido.recipenetwork.di.NetWorkManager

class Repository (private val manager: NetWorkManager) {

    suspend fun getRecipe() = manager.provideUnauthorizedCachedRequestsApi().getList()

    suspend fun getRecipeById(id: String) =
        manager.provideUnauthorizedCachedRequestsApi().getRecipe(id)
}