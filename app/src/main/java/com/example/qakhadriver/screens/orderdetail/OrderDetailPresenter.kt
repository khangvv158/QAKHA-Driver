package com.example.qakhadriver.screens.orderdetail

class OrderDetailPresenter : OrderDetailContract.Presenter {

    private var view: OrderDetailContract.View? = null

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: OrderDetailContract.View?) {
        this.view = view
    }
}
