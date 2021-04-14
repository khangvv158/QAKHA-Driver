package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class PhoneRequest(
        @SerializedName("phone_number") val phoneNumber: String
)
