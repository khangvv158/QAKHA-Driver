package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
        @SerializedName("new_password") val newPassword: String,
        @SerializedName("verification_code") val verificationCode: String
)
