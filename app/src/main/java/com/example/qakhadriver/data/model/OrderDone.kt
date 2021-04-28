package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDone(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nameUser: String,
    @SerializedName("phone_number") val phoneNumberUser: String,
    @SerializedName("address") val addressUser: String,
    @SerializedName("delivery_time") val deliveryTime: String?,
    @SerializedName("subtotal") val subtotal: Float = 0f,
    @SerializedName("discount") val discount: Float = 0f,
    @SerializedName("shipping_fee") val shippingFee: Float = 0f,
    @SerializedName("rate_status") val rateStatus: String,
    @SerializedName("total") val total: Float = 0f,
    @SerializedName("status") val status: String,
    @SerializedName("type_checkout") val typeCheckout: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("user_id") val userId: Int,
    @SerializedName("driver_id") val driverId: Int,
    @SerializedName("voucher_id") val voucherId: Int?,
    @SerializedName("partner_id") val partnerID: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("order_details") val carts: MutableList<Cart>,
    @SerializedName("partner") val partner: Partner
) : Parcelable
