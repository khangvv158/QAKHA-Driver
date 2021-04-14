package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("password_confirmation") val password_confirmation: String,
        @SerializedName("phone_number") val phone_number: String,
        @SerializedName("name") val name: String,
        @SerializedName("id_card") val idCard: String,
        @SerializedName("license_plate") var licensePlate: String,
        @SerializedName("type_user") var typeUser: String = "2",
        @SerializedName("city_id") var idCity: String = "1"
)
