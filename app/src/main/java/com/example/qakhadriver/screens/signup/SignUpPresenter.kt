package com.example.qakhadriver.screens.signup

import com.example.qakhadriver.data.repository.SignRepository
import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import com.example.qakhadriver.data.source.remote.schema.request.IdentifyCardRequest
import com.example.qakhadriver.data.source.remote.schema.request.LicensePlateRequest
import com.example.qakhadriver.data.source.remote.schema.request.PhoneRequest
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

class SignUpPresenter(private val signRepository: SignRepository) : SignUpContract.Presenter {

    private var view: SignUpContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun signUp(
            email: String,
            password: String,
            passwordConfirmation: String,
            phoneNumber: String,
            name: String,
            idCard: String,
            licensePlate: String
    ) {
        val disposable = signRepository.signUp(
                email,
                password,
                passwordConfirmation,
                phoneNumber,
                name,
                idCard,
                licensePlate
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onSignUpSuccess()
                }, {
                    if (it is HttpException) {
                        try {
                            view?.onSignUpFailure(Gson().fromJson(
                                    it.response()?.errorBody()?.string(),
                                    MessageResponse::class.java
                            ).message)
                        } catch (e: Exception) {
                            view?.onError(it.localizedMessage)
                        }
                    }
                })
        compositeDisposable.add(disposable)
    }

    override fun checkEmailIsExist(email: String) {
        val disposable = signRepository.checkEmail(EmailRequest(email))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        view?.onCheckEmailIsExistSuccess()
                    }
                }, {
                    view?.onCheckEmailIsExistFailure()
                })
        compositeDisposable.add(disposable)
    }

    override fun checkPhoneNumberIsExist(phoneNumber: String) {
        val disposable = signRepository.checkPhoneNumber(PhoneRequest(phoneNumber))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onCheckPhoneNumberIsExistSuccess()
                }, {
                    view?.onCheckPhoneNumberIsExistFailure()
                })
        compositeDisposable.add(disposable)
    }

    override fun checkIdCardIsExist(idCard: String) {
        val disposable = signRepository.checkIdentifyCard(IdentifyCardRequest(idCard))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onCheckIdCardIsExistSuccess()
                }, {
                    view?.onCheckIdCardIsExistFailure()
                })
        compositeDisposable.add(disposable)
    }

    override fun checkLicensePlate(licensePlate: String) {
        val disposable = signRepository.checkLicensePlate(LicensePlateRequest(licensePlate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onCheckLicensePlateSuccess()
                }, {
                    view?.onCheckLicensePlateFailure()
                })
        compositeDisposable.add(disposable)
    }

    override fun onStart() = Unit

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: SignUpContract.View?) {
        this.view = view
    }
}
