package com.example.qakhadriver.screens.signup

import com.example.qakhadriver.utils.BasePresenter

interface SignUpContract {

    interface View {

        fun onSignUpSuccess()
        fun onSignUpFailure(message: String)
        fun onCheckEmailIsExistSuccess()
        fun onCheckEmailIsExistFailure()
        fun onCheckPhoneNumberIsExistSuccess()
        fun onCheckPhoneNumberIsExistFailure()
        fun onCheckIdCardIsExistSuccess()
        fun onCheckIdCardIsExistFailure()
        fun onCheckLicensePlateSuccess()
        fun onCheckLicensePlateFailure()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun signUp(email: String,
                   password: String,
                   passwordConfirmation: String,
                   phoneNumber: String,
                   name: String,
                   idCard: String,
                   licensePlate: String
        )

        fun checkEmailIsExist(email: String)
        fun checkPhoneNumberIsExist(phoneNumber: String)
        fun checkIdCardIsExist(idCard: String)
        fun checkLicensePlate(licensePlate: String)
    }
}
