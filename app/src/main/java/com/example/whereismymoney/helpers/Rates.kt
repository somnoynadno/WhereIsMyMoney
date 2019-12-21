package com.example.whereismymoney.helpers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rates {
    @SerializedName("USD")
    @Expose
    public var USD: Double? = null
    @SerializedName("RUB")
    @Expose
    public var RUB: Double? = null
}