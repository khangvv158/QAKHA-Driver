package com.example.qakhadriver.screens.signup.activate

import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import com.example.qakhadriver.utils.BasePresenter

interface ActivateContract {

    interface View {

        fun onActivateAccountSuccess()
        fun onActivateAccountFailure()
        fun onResendCodeActivateSuccess()
        fun onResendCodeActivateFailure()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun activateAccount(codeActivate: String)
        fun resendCodeActivate(emailRequest: EmailRequest)
    }
}
