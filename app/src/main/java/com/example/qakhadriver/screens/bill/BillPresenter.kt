package com.example.qakhadriver.screens.bill

class BillPresenter (): BillContract.Presenter {

    private var view : BillContract.View? = null

    override fun onStart() = Unit

    override fun onStop() {
    }

    override fun setView(view: BillContract.View?) {
        this.view = view
    }
}
