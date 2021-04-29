package com.example.qakhadriver.screens.me

import com.example.qakhadriver.data.repository.IncomeRepository
import com.example.qakhadriver.data.repository.ProfileRepository
import com.example.qakhadriver.data.repository.TokenRepository
import com.example.qakhadriver.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MePresenter(
    private val tokenRepository: TokenRepository,
    private val profileRepository: ProfileRepository,
    private val incomeRepository: IncomeRepository
) : MeContract.Presenter {

    private var view: MeContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun checkStatusIsOffline() {
        val disposable = profileRepository.getStatus(tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    Constants.ONLINE -> view?.onCheckStatusIsOfflineFailure()
                    Constants.SHIPPING -> view?.onCheckStatusIsOfflineFailure()
                    Constants.OFFLINE -> view?.onCheckStatusIsOfflineSuccess()
                }
            }, {
                view?.onError(it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }

    override fun getCoin() {
        val disposable = incomeRepository.getCoin(tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetCoinSuccess(it)
            }, {
                view?.onError(it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }

    override fun signOut() {
        tokenRepository.clearToken()
        view?.onSignOutSuccess()
    }

    override fun onStart() {
    }

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: MeContract.View?) {
        this.view = view
    }
}
