package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.example.qakhadriver.utils.Constants.DEFAULT_FLOAT
import com.example.qakhadriver.utils.Constants.DEFAULT_STRING
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String = DEFAULT_STRING,
    @SerializedName("price") val price: Float = DEFAULT_FLOAT,
    @SerializedName("description") val description: String = DEFAULT_STRING,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("image") val image: Image
) : Parcelable
