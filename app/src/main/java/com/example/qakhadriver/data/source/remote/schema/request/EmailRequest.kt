package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("email") val email: String,
    @SerializedName("type_user") val type: Int = 2
)
