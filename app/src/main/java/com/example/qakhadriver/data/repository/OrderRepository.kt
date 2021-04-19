package com.example.qakhadriver.data.repository

import com.example.qakhadriver.utils.Constants
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase

interface OrderRepository {

    fun listenerOrder(idDriver: Int, callback: ChildEventListener)
}

class OrderRepositoryImpl : OrderRepository {

    private val firebaseClient = FirebaseDatabase.getInstance()

    override fun listenerOrder(idDriver: Int, callback: ChildEventListener) {
        firebaseClient.reference.child(Constants.DRIVERS)
            .child(Constants.SHIPPING)
            .child(idDriver.toString())
            .child(Constants.ORDER)
            .addChildEventListener(callback)
    }

    companion object {
        private var instance: OrderRepositoryImpl? = null

        fun getInstance(): OrderRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: OrderRepositoryImpl().also {
                    instance = it
                }
            }
    }
}
