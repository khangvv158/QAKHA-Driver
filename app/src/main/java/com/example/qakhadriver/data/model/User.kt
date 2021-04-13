package com.example.qakhadriver.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem

data class User(
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("latitude") var latitude: Float = 0f,
    @SerializedName("longitude") var longitude: Float = 0f
) : ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String? {
        return name
    }
}
