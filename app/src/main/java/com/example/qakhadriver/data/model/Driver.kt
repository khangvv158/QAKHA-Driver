package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Driver(
    @SerializedName("id") val idDriver: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("id_card") val icCard: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("license_plate") val licensePlate: String,
    @SerializedName("image") val image: Image,
    @SerializedName("status") val status: String
) : Parcelable
