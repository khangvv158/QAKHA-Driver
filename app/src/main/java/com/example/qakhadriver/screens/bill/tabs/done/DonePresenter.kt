package com.example.qakhadriver.screens.bill.tabs.done

import com.example.qakhadriver.data.repository.OrderRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DonePresenter(
    private val tokenRepository: TokenRepository,
    private val orderRepository: OrderRepository
) : DoneContract.Presenter {

    private var view: DoneContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getOrderDone() {
        val disposable = orderRepository.getOrderDone(tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetOrderDoneSuccess(it)
            }, {
                view?.onError(it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
    }

    override fun onStop() {
        view = null
        compositeDisposable.clear()
    }

    override fun setView(view: DoneContract.View?) {
        this.view = view
    }
}
