package com.svirido.recipenetwork

import android.app.Application
import com.svirido.recipenetwork.di.AppComponent
import com.svirido.recipenetwork.di.AppModule
import com.svirido.recipenetwork.di.DaggerAppComponent

class MyApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}