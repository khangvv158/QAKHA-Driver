package com.example.qakhadriver.screens.bill.tabs.freepick

import com.example.qakhadriver.data.model.OrderFirebase
import com.example.qakhadriver.utils.BasePresenter

interface FreePickContract {

    interface View {

        fun onGetOrderFirebaseSuccess(orderFirebase: OrderFirebase)
        fun onGetOrderFirebaseRemove()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getOrderFirebase(id: Int)
    }
}
