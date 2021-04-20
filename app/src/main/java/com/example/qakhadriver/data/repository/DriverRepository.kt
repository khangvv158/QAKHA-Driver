package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.DriverFirebase
import com.example.qakhadriver.utils.Constants
import com.google.firebase.database.*

interface DriverRepository {

    fun updateLocationDriver(driverFirebase: DriverFirebase)
}

class DriverRepositoryImpl private constructor(): DriverRepository {

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

    companion object {
        private var instance: DriverRepositoryImpl? = null

        fun getInstance(): DriverRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: DriverRepositoryImpl().also {
                    instance = it
                }
            }
    }
}
