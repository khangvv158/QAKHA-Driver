package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class CredentialRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
)
