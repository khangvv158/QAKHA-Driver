package com.example.qakhadriver.data.source.remote.schema.response

import com.google.gson.annotations.SerializedName

data class IncomeMonthResponse(
    @SerializedName("total_cash") val totalCash: Float,
    @SerializedName("total_coins") val totalCoin: Float,
    @SerializedName("total_paypal") val totalPayPal: Float,
    @SerializedName("total_monthly_income") val totalMonthly: Float
)
