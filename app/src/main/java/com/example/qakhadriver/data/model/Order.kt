package com.example.qakhadriver.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("order_details") val carts: MutableList<Cart>,
    @SerializedName("order") val orderDetail: OrderDetail,
    @SerializedName("location_user") val locationUser: LocationUser
) : Parcelable

@Parcelize
data class OrderDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nameUser: String,
    @SerializedName("phone_number") val phoneNumberUser: String,
    @SerializedName("address") val addressUser: String,
    @SerializedName("delivery_time") val deliveryTime: String?,
    @SerializedName("subtotal") val subtotal: Float = 0f,
    @SerializedName("discount") val discount: Float = 0f,
    @SerializedName("shipping_fee") val shippingFee: Float = 0f,
    @SerializedName("total") val total: Float = 0f,
    @SerializedName("status") val status: String,
    @SerializedName("type_checkout") val typeCheckout: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("user_id") val userId: Int,
    @SerializedName("driver_id") val driverId: Int,
    @SerializedName("voucher_id") val voucherId: Int?,
    @SerializedName("partner_id") val partnerID: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("partner") val partner: Partner
): Parcelable

@Parcelize
data class Cart(
    @SerializedName("id") val idCart: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Float,
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("product") val product: Product
) : Parcelable

@Parcelize
data class Product(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: Image
): Parcelable

@Parcelize
data class Partner(
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float
): Parcelable

@Parcelize
data class LocationUser(
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float
): Parcelable
