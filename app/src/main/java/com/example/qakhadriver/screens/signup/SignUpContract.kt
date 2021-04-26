package com.example.qakhadriver.screens.signup

import com.example.qakhadriver.utils.BasePresenter

interface SignUpContract {

    interface View {

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

        fun checkEmailIsExist(email: String)
        fun checkPhoneNumberIsExist(phoneNumber: String)
        fun checkIdCardIsExist(idCard: String)
        fun checkLicensePlate(licensePlate: String)
    }
}
