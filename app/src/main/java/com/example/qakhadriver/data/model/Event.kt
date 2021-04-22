package com.example.qakhadriver.data.model

data class Event<T>(
    val keyEvent: String,
    val obj: T
)
