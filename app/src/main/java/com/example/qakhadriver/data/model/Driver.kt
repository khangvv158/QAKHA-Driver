package com.example.qakhadriver.data.model

import com.google.gson.annotations.SerializedName

data class Driver(
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("id_card") val icCard: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("license_plate") val licensePlate: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("latitude") var latitude: Float = 0f,
    @SerializedName("longitude") var longitude: Float = 0f
)