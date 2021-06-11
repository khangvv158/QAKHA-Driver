package com.example.qakhadriver.screens.forgotpassword

import com.example.qakhadriver.utils.BasePresenter

interface ForgotPasswordContract {

    interface View {
        fun onForgotPasswordSuccess(email: String)
        fun onForgotPasswordFailure()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun forgotPassword(email: String)
    }
}
