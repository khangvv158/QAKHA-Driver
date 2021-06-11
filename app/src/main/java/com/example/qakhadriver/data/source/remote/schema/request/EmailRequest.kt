package com.example.qakhadriver.data.source.remote.schema.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmailRequest(
    @SerializedName("email") val email: String,
    @SerializedName("type_user") val type: Int = 2
) : Parcelable
