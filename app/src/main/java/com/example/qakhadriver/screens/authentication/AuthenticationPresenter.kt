package com.example.qakhadriver.screens.authentication

import com.example.qakhadriver.data.model.TokenAccess
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefs
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsKey

class AuthenticationPresenter(private val sharedPrefs: SharedPrefs)
    : AuthenticationContract.Presenter {

    private var view: AuthenticationContract.View? = null

    override fun checkSignedIn() {
        val token = sharedPrefs.get(SharedPrefsKey.TOKEN_KEY, TokenAccess::class.java)
        if (token != null) {
            view?.onCheckSignedInSuccess()
        } else {
            view?.onCheckSignedInFailure()
        }
    }

    override fun checkTokenSignedIn() {
    }

    override fun onStart() = Unit

    override fun onStop() {
    }

    override fun setView(view: AuthenticationContract.View?) {
        this.view = view
    }
}
