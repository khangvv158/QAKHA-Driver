package com.example.qakhadriver.screens.orderdetail

import com.example.qakhadriver.data.repository.OrderRepository
import com.example.qakhadriver.data.repository.TokenRepository
import com.example.qakhadriver.data.source.remote.schema.request.CompleteDelivery
import com.example.qakhadriver.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderDetailPresenter(
    private val orderRepository: OrderRepository,
    private val tokenRepository: TokenRepository
) : OrderDetailContract.Presenter {

    private var view: OrderDetailContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun completeDelivery(idOrder: Int, idDriver: Int) {
        val disposable = orderRepository.completeDelivery(
            CompleteDelivery(
                idOrder,
                idDriver,
                Constants.COMPLETED
            ), tokenRepository.getToken()
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.completeDeliverySuccess()
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

    override fun setView(view: OrderDetailContract.View?) {
        this.view = view
    }
}
