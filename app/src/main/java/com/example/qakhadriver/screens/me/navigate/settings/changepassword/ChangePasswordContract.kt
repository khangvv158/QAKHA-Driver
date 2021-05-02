package com.example.qakhadriver.screens.me.navigate.settings.changepassword

import com.example.qakhadriver.data.source.remote.schema.request.ChangePasswordRequest
import com.example.qakhadriver.utils.BasePresenter

interface ChangePasswordContract {

    interface View {

        fun onChangePasswordSuccess()
        fun onChangePasswordSuccessFailure(message: String)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun changePassword(changePasswordRequest: ChangePasswordRequest)
    }
}
