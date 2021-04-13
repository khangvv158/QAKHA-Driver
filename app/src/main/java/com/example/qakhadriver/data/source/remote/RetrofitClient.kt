package com.example.qakhadriver.data.source.remote

import com.google.gson.GsonBuilder
import com.example.qakhadriver.utils.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    private var baseRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T {
        return baseRetrofit.create(service)
    }

    companion object {

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
