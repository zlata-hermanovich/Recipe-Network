package com.svirido.recipenetwork.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.svirido.recipenetwork.R
import com.svirido.recipenetwork.databinding.ActivitySplashBinding
import com.svirido.recipenetwork.ui.mainscreen.MainActivity
import java.util.*
import kotlin.concurrent.schedule

const val TIME_SPLASH_SCREEN: Long = 3000

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.titleTextView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_anim_text))

        Timer().schedule(TIME_SPLASH_SCREEN) {
            start()
        }
    }
    private fun start() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}