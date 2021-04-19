package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.example.qakhadriver.utils.Constants.DEFAULT_STRING
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    @SerializedName("url") val imageUrl: String = DEFAULT_STRING
) : Parcelable
