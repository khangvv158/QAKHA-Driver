package com.example.qakhadriver.screens.bill.tabs.done

class DonePresenter : DoneContract.Presenter {

    private var view: DoneContract.View? = null

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: DoneContract.View?) {
        this.view = view
    }
}
