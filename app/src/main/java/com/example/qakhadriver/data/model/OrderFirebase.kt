package com.example.qakhadriver.data.model

import com.google.firebase.database.PropertyName

data class OrderFirebase(
        @set :PropertyName("order_id")
        @get :PropertyName("order_id")
        var id: Int = 0
)
