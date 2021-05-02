package com.example.qakhadriver.screens.me.navigate.settings.changepassword

import com.example.qakhadriver.data.repository.ProfileRepository
import com.example.qakhadriver.data.repository.TokenRepository
import com.example.qakhadriver.data.source.remote.schema.request.ChangePasswordRequest
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

class ChangePasswordPresenter(
    private val profileRepository: ProfileRepository,
    private val tokenRepository: TokenRepository
) :
    ChangePasswordContract.Presenter {

    private var view: ChangePasswordContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        val disposable =
            profileRepository.changePassword(changePasswordRequest, tokenRepository.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onChangePasswordSuccess()
                }, {
                    if (it is HttpException) {
                        try {
                            view?.onChangePasswordSuccessFailure(
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

    override fun onStart() {
    }

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: ChangePasswordContract.View?) {
        this.view = view
    }
}
