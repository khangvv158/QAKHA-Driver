package com.example.qakhadriver.screens.authentication

import com.example.qakhadriver.utils.BasePresenter

interface AuthenticationContract {

    interface View {
        fun onCheckSignedInSuccess()
        fun onCheckSignedInFailure()
        fun onCheckTokenSignedInSuccess()
        fun onCheckTokenSignedInFailure()
    }

    interface Presenter : BasePresenter<View> {
        fun checkSignedIn()
        fun checkTokenSignedIn()
    }
}
