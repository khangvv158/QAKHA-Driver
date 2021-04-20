package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.data.source.remote.OrderAPI
import com.example.qakhadriver.data.source.remote.RetrofitClient
import com.example.qakhadriver.data.source.remote.schema.request.CompleteDelivery
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface OrderRepository {

    fun getOrderDetail(idDriver: Int, idOrder: Int, token: String): Single<Order>
    fun completeDelivery(
        completeDelivery: CompleteDelivery,
        token: String
    ): Observable<MessageResponse>
}

class OrderRepositoryImpl private constructor() : OrderRepository {

    private val client = RetrofitClient.getInstance().create(OrderAPI::class.java)

    override fun getOrderDetail(idDriver: Int, idOrder: Int, token: String): Single<Order> =
        client.getOrderDetail(idDriver, idOrder, token)

    override fun completeDelivery(
        completeDelivery: CompleteDelivery,
        token: String
    ): Observable<MessageResponse> = client.completeDelivery(completeDelivery, token)

    companion object {

        @Volatile
        private var instance: OrderRepositoryImpl? = null

        fun getInstance(): OrderRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: OrderRepositoryImpl().also {
                    instance = it
                }
            }
    }
}
