package com.example.qakhadriver.data.source.remote.schema.request

import com.google.gson.annotations.SerializedName

data class CompleteDelivery(
    @SerializedName("order_id") val idOrder: Int,
    @SerializedName("driver_id") val idDriver: Int,
    @SerializedName("status_order") val statusOrder: String
)
