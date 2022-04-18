package com.svirido.recipenetwork.repository

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.svirido.recipenetwork.model.Hit
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

private const val FILE_NAME = "recipe_network"
private const val RECIPE_ID = "recipe_id"

class PreferencesRepository @Inject constructor(private val context: Context) {

    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun saveRecipe(recipe: Hit) {
        val list = favoriteJsonToArray()?: arrayListOf()
        list.add(recipe)
        preferences.edit {
            putString(RECIPE_ID, Gson().toJson(list))
            apply()
        }
    }

    fun saveRecipeAuthUser(recipe: Hit, uid:String) {
        val list = favoriteJsonToArrayAuthUser(uid)?: arrayListOf()
        list.add(recipe)
        context.getSharedPreferences(uid,Context.MODE_PRIVATE).edit {
            putString(RECIPE_ID, Gson().toJson(list))
            apply()
        }
    }

    fun deleteRecipe(recipe: Hit) {
        val list = favoriteJsonToArray()
        list?.remove(recipe)
        preferences.edit {
            putString(RECIPE_ID, Gson().toJson(list))
            apply()
        }
    }

    fun deleteRecipeAuthUser(recipe: Hit, uid:String) {
        val list = favoriteJsonToArrayAuthUser(uid)
        list?.remove(recipe)
        context.getSharedPreferences(uid,Context.MODE_PRIVATE).edit {
            putString(RECIPE_ID, Gson().toJson(list))
            apply()
        }
    }

    fun favoriteJsonToArray(): ArrayList<Hit>? {
        val recipeJson = preferences.getString(RECIPE_ID, "")
        val recipeListType: Type =
            object : TypeToken<List<Hit?>?>() {}.type
        return Gson().fromJson<List<Hit>>(recipeJson, recipeListType) as ArrayList<Hit>?
    }

    fun favoriteJsonToArrayAuthUser(uid:String): ArrayList<Hit>? {
        val recipeJson =  context.getSharedPreferences(uid,Context.MODE_PRIVATE).getString(RECIPE_ID, "")
        val recipeListType: Type =
            object : TypeToken<List<Hit?>?>() {}.type
        return Gson().fromJson<List<Hit>>(recipeJson, recipeListType) as ArrayList<Hit>?
    }
}