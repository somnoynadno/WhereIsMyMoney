package com.example.whereismymoney.helpers.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


object NetworkClient {
    val API_URL = "http://data.fixer.io"
    var retrofit: Retrofit? = null

    val retrofitClient: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}