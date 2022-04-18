package com.svirido.recipenetwork.ui.mainscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.svirido.recipenetwork.R
import com.svirido.recipenetwork.databinding.ActivityMainBinding
import com.svirido.recipenetwork.databinding.HeaderMenuBinding
import com.svirido.recipenetwork.ui.authorization.AuthorizationActivity
import com.svirido.recipenetwork.ui.favorite.FavoriteFragment
import com.svirido.recipenetwork.ui.mainscreen.recipescreen.RecipeFragment
import com.svirido.recipenetwork.util.loadLocate
import com.svirido.recipenetwork.util.showChangeLanguage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingHeader: HeaderMenuBinding
    private lateinit var auth: FirebaseAuth

    private fun handleDeepLink(intent: Intent) {
        intent.data?.pathSegments?.get(1)?.split("-")?.last()?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerMain, RecipeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ID_KEY, it)
                    }
                })
                .commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLink(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            intent?.let { handleDeepLink(it) }
        }

        auth = FirebaseAuth.getInstance()

        bindingHeader = HeaderMenuBinding.inflate(layoutInflater)

        if (auth.currentUser!=null){
            binding.menuRecipe.menu.clear()
            binding.menuRecipe.inflateMenu(R.menu.menu_auth_user)
        }else{
            binding.menuRecipe.menu.clear()
            binding.menuRecipe.inflateMenu(R.menu.recipe_menu)
        }
        binding.menuRecipe.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_page_menu -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.favorite_menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.containerMain, FavoriteFragment())
                        .commit()
                }
                R.id.logout_menu -> {
                    auth.signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Log out", Toast.LENGTH_LONG).show()
                }
                R.id.change_language_menu -> {
                    showChangeLanguage()
                }
                R.id.sign_in_menu -> {
                    startActivity(Intent(this, AuthorizationActivity::class.java))
                }
            }
            true
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerMain, MainFragment())
            .commit()
    }
}