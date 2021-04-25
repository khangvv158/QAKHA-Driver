package com.example.qakhadriver.screens.earnings.tabs.day

import com.example.qakhadriver.data.repository.IncomeRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IncomeDayPresenter(
    private val incomeRepository: IncomeRepository,
    private val tokenRepository: TokenRepository
) : IncomeDayContract.Presenter {

    private var view: IncomeDayContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getIncomeDay(day: String) {
        val disposable = incomeRepository.getIncomeByDay(day, tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetIncomeDaySuccess(it)
            }, {
                view?.onError(it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
    }

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: IncomeDayContract.View?) {
        this.view = view
    }
}
