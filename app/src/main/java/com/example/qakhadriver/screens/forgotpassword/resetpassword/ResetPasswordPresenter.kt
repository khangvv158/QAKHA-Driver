package com.example.qakhadriver.screens.forgotpassword.resetpassword

import com.example.qakhadriver.data.repository.SignRepository
import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import com.example.qakhadriver.data.source.remote.schema.request.ResetPasswordRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ResetPasswordPresenter(private val signRepository: SignRepository) :
    ResetPasswordContract.Presenter {

    private var view: ResetPasswordContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun resetPassword(newPassword: String, verificationCode: String) {
        val disposable = signRepository.resetPasswordByVerificationCode(
            ResetPasswordRequest(newPassword, verificationCode)
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onResetPasswordSuccess()
            }, {
                view?.onResetPasswordFailure()
            })
        compositeDisposable.add(disposable)
    }

    override fun generatingCode(email: String) {
        val disposable = signRepository.forgotPassword(EmailRequest(email))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGeneratingCodeSuccess()
            }, {
                view?.onGeneratingCodeFailure()
            })
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
    }

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: ResetPasswordContract.View?) {
        this.view = view
    }
}
