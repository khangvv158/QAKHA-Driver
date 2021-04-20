package com.example.qakhadriver.screens.authentication

import com.example.qakhadriver.data.repository.ProfileRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthenticationPresenter(
    private val tokenRepository: TokenRepository,
    private val profileRepository: ProfileRepository
) :
    AuthenticationContract.Presenter {

    private var view: AuthenticationContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun checkSignedInByToken() {
        if (tokenRepository.getToken().isNotEmpty()) {
            view?.onCheckSignedInByTokenSuccess()
        } else {
            view?.onCheckSignedInByTokenFailure()
        }
    }

    override fun getProfile() {
        val disposable = profileRepository.getDriver(tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetProfileSuccess(it)
            }, {
                view?.onCheckSignedInByTokenFailure()
            })
        compositeDisposable.add(disposable)
    }

    override fun onStart() = Unit

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: AuthenticationContract.View?) {
        this.view = view
    }
}
