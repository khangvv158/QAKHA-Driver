package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.data.model.OrderDone
import com.example.qakhadriver.data.source.remote.schema.request.CompleteDelivery
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import com.example.qakhadriver.utils.Constants.AUTHORIZATION
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface OrderAPI {

    @GET("drivers/show_infor")
    fun getOrderDetail(
        @Query("id") idDriver: Int,
        @Query("order_id") idOrder: Int,
        @Header(AUTHORIZATION) token: String
    ): Single<Order>

    @PATCH("drivers/complete_delivery")
    fun completeDelivery(
        @Body completeDelivery: CompleteDelivery,
        @Header(AUTHORIZATION) token: String
    ): Observable<MessageResponse>

    @GET("drivers/order_history")
    fun getOrderDone(
        @Header(AUTHORIZATION) token: String
    ): Single<MutableList<OrderDone>>
}
