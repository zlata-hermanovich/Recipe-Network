package com.svirido.recipenetwork.ui.authorization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.svirido.recipenetwork.R

class AuthorizationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        supportFragmentManager.beginTransaction()
            .replace(R.id.authorizationContainer, AuthorizationFragment()).commit()
    }
}