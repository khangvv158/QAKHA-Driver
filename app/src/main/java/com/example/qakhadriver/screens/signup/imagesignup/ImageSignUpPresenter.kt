package com.example.qakhadriver.screens.signup.imagesignup

import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.qakhadriver.data.repository.CloudRepository
import com.example.qakhadriver.data.repository.SignRepository
import com.example.qakhadriver.data.source.remote.schema.request.RegisterRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ImageSignUpPresenter(
    private val signRepository: SignRepository,
    private val cloudRepository: CloudRepository
) : ImageSignUpContract.Presenter {

    private var view: ImageSignUpContract.View? = null
    private val compositeDisposable = CompositeDisposable()

    override fun signUp(registerRequest: RegisterRequest) {
        registerRequest.apply {
            val disposable = signRepository.signUp(
                email,
                password,
                password_confirmation,
                phone_number,
                name,
                idCard,
                licensePlate,
                image
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onSignUpSuccess()
                }, {
                    view?.onSignUpFailure(it)
                })
            compositeDisposable.add(disposable)
        }
    }

    override fun uploadFileToCloud(file: String) {
        cloudRepository.uploadFileToCloud(file, object : UploadCallback {

            override fun onStart(requestId: String?) = Unit

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) = Unit

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                view?.uploadFileToCloudSuccess(resultData?.get("url").toString())
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                error?.let { view?.onError(it.description) }
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                error?.let { view?.onError(it.description) }
            }
        })
    }

    override fun onStart() {
        compositeDisposable.clear()
    }

    override fun onStop() {
    }

    override fun setView(view: ImageSignUpContract.View?) {
        this.view = view
    }
}
