package com.example.qakhadriver.screens.signup.imagesignup

import com.example.qakhadriver.data.source.remote.schema.request.RegisterRequest
import com.example.qakhadriver.utils.BasePresenter

interface ImageSignUpContract {

    interface View {

        fun onSignUpSuccess()
        fun onSignUpFailure(throwable: Throwable)
        fun uploadFileToCloudSuccess(url: String)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun signUp(registerRequest: RegisterRequest)

        fun uploadFileToCloud(file: String)
    }
}
