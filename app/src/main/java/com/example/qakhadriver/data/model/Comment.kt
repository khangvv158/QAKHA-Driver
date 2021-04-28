package com.example.qakhadriver.data.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id") val idComment: Int,
    @SerializedName("content") val content: String,
    @SerializedName("point") val point: Int = 0,
    @SerializedName("created_at") val timeFeedback: String,
    @SerializedName("order_id") val idOrder: Int,
    @SerializedName("user") val user: UserComment
)

data class UserComment(
    @SerializedName("name") val name: String,
)
