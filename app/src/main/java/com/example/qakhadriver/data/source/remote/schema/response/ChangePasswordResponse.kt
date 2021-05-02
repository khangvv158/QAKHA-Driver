package com.example.qakhadriver.data.source.remote.schema.response

import com.example.qakhadriver.data.model.Image
import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("id") val idUser: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("image") val image: Image,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("license_plate") val licensePlate: String,
    @SerializedName("coins") val coin: Float = 0f,
)
