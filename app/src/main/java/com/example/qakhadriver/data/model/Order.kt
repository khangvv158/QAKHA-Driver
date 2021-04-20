package com.example.qakhadriver.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id") val idOrder: Int,
    @SerializedName("name") val nameDriver: String,
    @SerializedName("phone_number") val phoneNumber: String,
)
