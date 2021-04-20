package com.example.qakhadriver.screens.bill

import com.example.qakhadriver.data.model.Status
import com.example.qakhadriver.data.repository.ProfileRepository
import com.example.qakhadriver.data.repository.TokenRepository
import com.example.qakhadriver.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BillPresenter(
    private val profileRepository: ProfileRepository,
    private val tokenRepository: TokenRepository
) : BillContract.Presenter {

    private var view: BillContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getStatus() {
        val disposable = profileRepository.getStatus(tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    Constants.ONLINE -> view?.onGetStatusOnline()
                    Constants.SHIPPING -> view?.onGetStatusOnline()
                    Constants.OFFLINE -> view?.onGetStatusOffline()
                }
            }, {
                view?.onError(it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }

    override fun goOnline(idDriver: Int) {
        val disposable = profileRepository.goOnlineOffline(
            idDriver,
            Status(Constants.ONLINE),
            tokenRepository.getToken()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGoOnlineSuccess()
            }, {
                view?.onError(it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }

    override fun goOffline(idDriver: Int) {
        val disposable = profileRepository.goOnlineOffline(
            idDriver,
            Status(Constants.OFFLINE),
            tokenRepository.getToken()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGoOfflineSuccess()
            }, {
                view?.onGoOfflineFailure()
            })
        compositeDisposable.add(disposable)
    }

    override fun onStart() = Unit

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: BillContract.View?) {
        this.view = view
    }
}
