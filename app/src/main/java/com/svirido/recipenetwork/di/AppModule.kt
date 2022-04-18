package com.svirido.recipenetwork.di

import android.content.Context
import com.svirido.recipenetwork.MyApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(
    private val app: MyApplication
) {
    @Provides
    fun provideContext(): Context = app
}