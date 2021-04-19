package com.example.qakhadriver.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DriverFirebase(
        val id: Int,
        val latitude: Float = 0f,
        val longitude: Float = 0f,
) : Parcelable
