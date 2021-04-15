package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenAccess(
        @SerializedName("message") val message: String?,
        @SerializedName("token") val token: String?
) : Parcelable
