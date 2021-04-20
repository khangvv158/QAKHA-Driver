package com.example.qakhadriver.screens.bill

import com.example.qakhadriver.utils.BasePresenter

interface BillContract {

    interface View {
        fun onGetStatusOnline()
        fun onGetStatusOffline()
        fun onGoOnlineSuccess()
        fun onGoOfflineSuccess()
        fun onGoOfflineFailure()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun getStatus()
        fun goOnline(idDriver: Int)
        fun goOffline(idDriver: Int)
    }
}
