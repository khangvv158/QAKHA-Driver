package com.example.qakhadriver.screens.signup.activate

import com.example.qakhadriver.utils.BasePresenter

interface ActivateContract {

    interface View {

        fun onActivateAccountSuccess()
        fun onActivateAccountFailure()
    }

    interface Presenter : BasePresenter<View> {

        fun activateAccount(codeActivate: String)
    }
}
