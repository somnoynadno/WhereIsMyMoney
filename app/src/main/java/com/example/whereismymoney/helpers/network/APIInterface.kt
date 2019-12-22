package com.example.whereismymoney.helpers.network

import retrofit2.Call
import retrofit2.http.GET


interface APIInterface {
    @GET("/api/latest?access_key=0b9b562eec577b98d38254155f31efef")
    fun getRates(): Call<APIResponse>
}