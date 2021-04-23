package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class ActivateRequest(
    @SerializedName("code_activate") val codeActivate: String
)
