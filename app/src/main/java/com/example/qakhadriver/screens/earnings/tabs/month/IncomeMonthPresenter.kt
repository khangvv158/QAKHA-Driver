package com.example.qakhadriver.screens.earnings.tabs.month

import com.example.qakhadriver.data.repository.IncomeRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IncomeMonthPresenter(
    private val incomeRepository: IncomeRepository,
    private val tokenRepository: TokenRepository
) : IncomeMonthContract.Presenter {

    private var view: IncomeMonthContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getIncomeMonth(date: String) {
        val disposable = incomeRepository.getIncomeByMonth(date, tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetIncomeMonthSuccess(it)
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

    override fun setView(view: IncomeMonthContract.View?) {
        this.view = view
    }
}
