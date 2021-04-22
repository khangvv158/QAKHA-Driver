package com.example.qakhadriver.screens.bill.tabs.freepick

import com.example.qakhadriver.data.repository.OrderRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FreePickPresenter(
    private val orderRepository: OrderRepository,
    private val tokenRepository: TokenRepository
) : FreePickContract.Presenter {

    private var view: FreePickContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getOrderDetail(idDriver: Int, idOrder: Int) {
        val disposable =
            orderRepository.getOrderDetail(idDriver, idOrder, tokenRepository.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onGetOrderDetailSuccess(it)
                }, {
                    view?.onError(it.localizedMessage)
                })
        compositeDisposable.add(disposable)
    }

    override fun onStart() = Unit

    override fun onStop() = Unit

    override fun setView(view: FreePickContract.View?) {
        this.view = view
    }
}
