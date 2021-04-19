package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.DriverFirebase
import com.example.qakhadriver.utils.Constants
import com.google.firebase.database.*

interface DriverRepository {

    fun updateLocationDriver(driverFirebase: DriverFirebase)
    fun listenerOrder(idDriver: Int, callback: ChildEventListener)
}

class DriverRepositoryImpl : DriverRepository {

    private val firebaseClient = FirebaseDatabase.getInstance()

    override fun updateLocationDriver(driverFirebase: DriverFirebase) {
        driverFirebase.id.let {
            firebaseClient.reference.child(Constants.DRIVERS)
                    .child(Constants.LOCATION)
                    .child(Constants.DRIVER)
                    .child(it.toString())
                    .setValue(driverFirebase)
        }
    }

    override fun listenerOrder(idDriver: Int, callback: ChildEventListener) {
        firebaseClient.reference.child(Constants.DRIVERS)
                .child(Constants.SHIPPING)
                .child(idDriver.toString())
                .child(Constants.ORDER)
                .addChildEventListener(callback)
    }

    companion object {
        private var instance: DriverRepository? = null

        fun getInstance(): DriverRepository =
                instance ?: synchronized(this) {
                    instance ?: DriverRepositoryImpl().also {
                        instance = it
                    }
                }
    }
}
