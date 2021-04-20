package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.data.source.remote.schema.request.CompleteDelivery
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import com.example.qakhadriver.utils.Constants.AUTHORIZATION
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OrderAPI {

    @GET("drivers/show_infor")
    fun getOrderDetail(
        @Query("id") idDriver: Int,
        @Query("order_id") idOrder: Int,
        @Header(AUTHORIZATION) token: String
    ): Single<Order>

    @GET("drivers/complete_delivery")
    fun completeDelivery(
        @Body completeDelivery: CompleteDelivery,
        @Header(AUTHORIZATION) token: String
    ): Observable<MessageResponse>
}
