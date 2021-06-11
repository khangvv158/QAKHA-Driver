package com.example.qakhadriver.screens.signup.activate

import com.example.qakhadriver.data.repository.SignRepository
import com.example.qakhadriver.data.source.remote.schema.request.ActivateRequest
import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ActivatePresenter(private val signRepository: SignRepository) : ActivateContract.Presenter {

    private var view: ActivateContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun activateAccount(codeActivate: String) {
        val disposable = signRepository.activateAccount(ActivateRequest(codeActivate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onActivateAccountSuccess()
            }, {
                view?.onActivateAccountFailure()
            })
        compositeDisposable.add(disposable)
    }

    override fun resendCodeActivate(emailRequest: EmailRequest) {
        val disposable = signRepository.resendCodeActivate(emailRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onResendCodeActivateSuccess()
            }, {
                view?.onActivateAccountFailure()
            })
        compositeDisposable.add((disposable))
    }

    override fun onStart() = Unit

    override fun onStop() {
        compositeDisposable.clear()
    }

    override fun setView(view: ActivateContract.View?) {
        this.view = view
    }
}
