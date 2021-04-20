package com.example.qakhadriver.data.source.remote.schema.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: String
)
