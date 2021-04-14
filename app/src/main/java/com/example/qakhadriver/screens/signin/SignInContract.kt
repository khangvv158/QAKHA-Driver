package com.example.qakhadriver.screens.signin

import com.example.qakhadriver.utils.BasePresenter

interface SignInContract {

    interface View {
        fun onSignInSuccess()
        fun onSignInFailure(message: String)
        fun onError(message: String)
        fun onSignInRoleFailure()
    }

    interface Presenter : BasePresenter<View> {
        fun signIn(email: String, password: String)
    }
}
