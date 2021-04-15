package com.example.qakhadriver.screens.signin

import com.example.qakhadriver.data.repository.SignRepository
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import com.example.qakhadriver.utils.Constants
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

class SignInPresenter(private val repository: SignRepository) : SignInContract.Presenter {

    private var view: SignInContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun signIn(email: String, password: String) {
        val disposable = repository.signIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        view?.onSignInSuccess()
                }, {
                    if (it is HttpException) {
                        try {
                            view?.onSignInFailure(
                                    Gson().fromJson(
                                            it.response()?.errorBody()?.string(),
                                            MessageResponse::class.java
                                    ).message
                            )
                        } catch (e: Exception) {
                            view?.onError(it.localizedMessage)
                        }
                    }
                })
        compositeDisposable.add(disposable)
    }

    override fun onStart() = Unit

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: SignInContract.View?) {
        this.view = view
    }
}
