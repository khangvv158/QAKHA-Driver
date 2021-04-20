package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Status
import com.example.qakhadriver.data.source.remote.ProfileAPI
import com.example.qakhadriver.data.source.remote.RetrofitClient
import io.reactivex.rxjava3.core.Observable

interface ProfileRepository {

    fun getDriver(token: String): Observable<Driver>
    fun getStatus(token: String): Observable<Status>
    fun goOnlineOffline(idDriver: Int, status: Status, token: String): Observable<Boolean>
}

class ProfileRepositoryImpl private constructor(): ProfileRepository {

    private val client = RetrofitClient.getInstance().create(ProfileAPI::class.java)

    override fun getDriver(token: String) = client.getDriver(token)

    override fun getStatus(token: String): Observable<Status> = client.getStatus(token)

    override fun goOnlineOffline(idDriver: Int, status: Status, token: String) =
        client.updateStatusDriver(idDriver, status, token)

    companion object {

        @Volatile
        private var instance: ProfileRepositoryImpl? = null

        fun getInstance(): ProfileRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: ProfileRepositoryImpl().also {
                    instance = it
                }
            }
    }
}
