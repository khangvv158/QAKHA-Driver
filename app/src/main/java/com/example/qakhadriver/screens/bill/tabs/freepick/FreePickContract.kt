package com.example.qakhadriver.screens.bill.tabs.freepick

import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.utils.BasePresenter

interface FreePickContract {

    interface View {

        fun onGetOrderDetailSuccess(order: Order)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getOrderDetail(idDriver: Int, idOrder: Int)
    }
}
