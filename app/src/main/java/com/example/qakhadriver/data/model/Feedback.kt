package com.example.qakhadriver.data.model

import com.google.gson.annotations.SerializedName

data class Feedback(
    @SerializedName("feedbacks") val comments: MutableList<Comment>,
    @SerializedName("avg_point") val point: Float = 0f
)
