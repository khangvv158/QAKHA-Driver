package com.example.qakhadriver.screens.earnings.tabs.year

import com.example.qakhadriver.data.repository.IncomeRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IncomeYearPresenter(
    private val incomeRepository: IncomeRepository,
    private val tokenRepository: TokenRepository
) : IncomeYearContract.Presenter {

    private var view: IncomeYearContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getIncomeYear(year: String) {
        val disposable = incomeRepository.getIncomeByYear(year, tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetIncomeYearSuccess(it)
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

    override fun setView(view: IncomeYearContract.View?) {
        this.view = view
    }
}
