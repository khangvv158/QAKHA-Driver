package com.example.qakhadriver.screens.main

import com.example.qakhadriver.data.repository.LanguageRepository

class MainActivityPresenter(
    private var languageRepository: LanguageRepository
) : MainActivityContract.Presenter {

    private var view: MainActivityContract.View? = null

    override fun onSetupLanguage() {
        languageRepository.getLanguageCode().let {
            view?.onSetupLanguageSuccess(it)
        }
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: MainActivityContract.View?) {
        this.view = view
    }
}
