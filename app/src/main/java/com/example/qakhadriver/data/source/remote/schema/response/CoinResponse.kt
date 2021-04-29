package com.example.qakhadriver.data.source.remote.schema.response

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("coins") val coin: Float = 0f
)
