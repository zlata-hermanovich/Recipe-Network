package com.svirido.recipenetwork.network

import com.svirido.recipenetwork.model.Hit
import com.svirido.recipenetwork.model.RecipeResponse
import com.svirido.recipenetwork.repository.Search
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val TYPE = "public"
private const val APP_ID = "81cc0c07"
private const val APP_KEY = "2aa68e51766d790c63b3b68c954e7547"

interface ApiService {

    @GET("api/recipes/v2")
    suspend fun getList(
        @Query("type") type: String? = TYPE,
        @Query("q") q: String? = Search.search,
        @Query("cuisineType") cuisineType: String? = Search.cuisineType,
        @Query("dishType") dishType: String? = Search.dishType,
        @Query("mealType") mealType: String? = Search.mealType,
        @Query("app_id") app_id: String? = APP_ID,
        @Query("app_key") app_key: String? = APP_KEY
    ): Response<RecipeResponse>

    @GET("api/recipes/v2/{id}")
    suspend fun getRecipe(
        @Path("id") id: String,
        @Query("type") type: String? = TYPE,
        @Query("app_id") app_id: String? = APP_ID,
        @Query("app_key") app_key: String? = APP_KEY
    ): Response<Hit>
}