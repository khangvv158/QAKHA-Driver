package com.example.qakhadriver.screens.feedback

import com.example.qakhadriver.data.repository.FeedbackRepository
import com.example.qakhadriver.data.repository.TokenRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class FeedbackPresenter(
    private val feedbackRepository: FeedbackRepository,
    private val tokenRepository: TokenRepository
) : FeedbackContract.Presenter {

    private var view: FeedbackContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun getFeedback(idDriver: Int) {
        val disposable = feedbackRepository.getFeedback(idDriver, tokenRepository.getToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onGetFeedbackSuccess(it)
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

    override fun setView(view: FeedbackContract.View?) {
        this.view = view
    }
}
