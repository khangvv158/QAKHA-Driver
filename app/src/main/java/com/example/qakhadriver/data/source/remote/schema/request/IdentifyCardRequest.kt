package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class IdentifyCardRequest(
        @SerializedName("id_card") val idCard : String
)
