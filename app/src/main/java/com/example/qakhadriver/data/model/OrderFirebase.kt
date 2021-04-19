package com.example.qakhadriver.data.model

import com.google.firebase.database.PropertyName

data class OrderFirebase(
        @set :PropertyName("driver_id")
        @get :PropertyName("driver_id")
        var idDriver: Int = 0,
        @set :PropertyName("order_id")
        @get :PropertyName("order_id")
        var idOrder: Int = 0
)
