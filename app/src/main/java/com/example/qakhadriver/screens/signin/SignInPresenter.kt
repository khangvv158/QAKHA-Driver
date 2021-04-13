package com.example.qakhadriver.screens.signin

class SignInPresenter : SignInContract.Presenter {

    private var view: SignInContract.View? = null

    override fun onStart() = Unit

    override fun onStop() = Unit

    override fun setView(view: SignInContract.View?) {
        this.view = view
    }
}
