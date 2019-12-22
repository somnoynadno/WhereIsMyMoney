package com.example.whereismymoney.helpers.network

import com.google.gson.annotations.*

// I just don't wanna fuck with getters
class APIResponse {
    @SerializedName("success")
    @Expose
    public var success: Boolean? = null
    @SerializedName("timestamp")
    @Expose
    public var timestamp: Int? = null
    @SerializedName("base")
    @Expose
    public var base: String? = null
    @SerializedName("date")
    @Expose
    public var date: String? = null
    @SerializedName("rates")
    @Expose
    public var rates: Rates? = null
}