package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class LicensePlateRequest(
        @SerializedName("license_plate") val licensePlate: String
)
