package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Status
import com.example.qakhadriver.utils.Constants.AUTHORIZATION
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface ProfileAPI {

    @GET("drivers")
    fun getDriver(
        @Header(AUTHORIZATION) token: String
    ): Observable<Driver>

    @GET("drivers/status_driver")
    fun getStatus(
        @Header(AUTHORIZATION) token: String
    ): Observable<Status>

    @PATCH("drivers/{id_driver}")
    fun updateStatusDriver(
        @Path("id_driver") idDriver: Int,
        @Body status: Status,
        @Header(AUTHORIZATION) token: String
    ) : Observable<Boolean>
}
