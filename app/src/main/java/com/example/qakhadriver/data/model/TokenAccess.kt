package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenAccess(
        @SerializedName("role") val role: String?,
        @SerializedName("token") val token: String?
) : Parcelable
