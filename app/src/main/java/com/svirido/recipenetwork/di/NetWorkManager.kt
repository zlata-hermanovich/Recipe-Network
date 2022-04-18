package com.svirido.recipenetwork.di

import android.util.Log
import com.svirido.recipenetwork.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.edamam.com/"

class NetWorkManager {

    fun provideUnauthorizedCachedRequestsApi(): ApiService {
        val okHttpClient = HttpClientFactory()
            .createHttpClient()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    inner class HttpClientFactory {

        fun createHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor { message ->
                        Log.d(
                            "OK_HTTP", message
                        )
                    }.apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                    chain.proceed(request.build())
                }
            return builder.build()
        }
    }
}