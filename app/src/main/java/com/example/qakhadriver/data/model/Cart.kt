package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.example.qakhadriver.utils.Constants.DEFAULT_QUANTITY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val product: Product,
    var quantity: Int = DEFAULT_QUANTITY,
    val partnerId: Int
) : Parcelable
