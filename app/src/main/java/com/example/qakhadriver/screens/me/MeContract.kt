package com.example.qakhadriver.screens.me

import com.example.qakhadriver.utils.BasePresenter

interface MeContract {

    interface View {

        fun onSignOutSuccess()
        fun onCheckStatusIsOfflineSuccess()
        fun onCheckStatusIsOfflineFailure()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun checkStatusIsOffline()
        fun signOut()
    }
}
