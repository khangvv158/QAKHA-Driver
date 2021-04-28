package com.example.qakhadriver.data.source.remote

import com.google.gson.GsonBuilder
import com.example.qakhadriver.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {

    private var baseRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(
            OkHttpClient
                .Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build()
        )
        .build()

    fun <T> create(service: Class<T>): T {
        return baseRetrofit.create(service)
    }

    companion object {

        private const val CONNECT_TIMEOUT = 60L
        private const val READ_TIMEOUT = 30L

        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RetrofitClient().also {
                    instance = it
                }
            }
    }
}
