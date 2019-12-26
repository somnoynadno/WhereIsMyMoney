package com.example.whereismymoney.helpers.network

import com.google.gson.annotations.*

data class APIResponse (
    @SerializedName("success")
    @Expose
    var success: Boolean? = null,
    @SerializedName("timestamp")
    @Expose
    var timestamp: Int? = null,
    @SerializedName("base")
    @Expose
    var base: String? = null,
    @SerializedName("date")
    @Expose
    var date: String? = null,
    @SerializedName("rates")
    @Expose
    var rates: Rates? = null
)