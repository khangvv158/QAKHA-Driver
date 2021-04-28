package com.example.qakhadriver.screens.bill.tabs.done

import com.example.qakhadriver.data.model.OrderDone
import com.example.qakhadriver.utils.BasePresenter

interface DoneContract {

    interface View {

        fun onGetOrderDoneSuccess(ordersDone: MutableList<OrderDone>)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getOrderDone()
    }
}
