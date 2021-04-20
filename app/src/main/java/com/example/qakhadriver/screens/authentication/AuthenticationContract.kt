package com.example.qakhadriver.screens.authentication

import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.utils.BasePresenter

interface AuthenticationContract {

    interface View {
        fun onCheckSignedInByTokenSuccess()
        fun onCheckSignedInByTokenFailure()
        fun onGetProfileSuccess(driver: Driver)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun checkSignedInByToken()
        fun getProfile()
    }
}
