package com.example.qakhadriver.screens.orderdetail

import com.example.qakhadriver.utils.BasePresenter

interface OrderDetailContract {

    interface View {

        fun completeDeliverySuccess()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun completeDelivery(idOrder: Int, idDriver: Int)
    }
}
