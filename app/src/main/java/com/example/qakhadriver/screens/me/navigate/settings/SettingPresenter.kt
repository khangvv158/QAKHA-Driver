package com.example.qakhadriver.screens.me.navigate.settings

import com.example.qakhadriver.data.repository.LanguageRepository

class SettingPresenter(private val languageRepository: LanguageRepository) :
    SettingContract.Presenter {

    private var view: SettingContract.View? = null

    override fun getCurrentLanguage() {
        languageRepository.getLanguageCode().let {
            view?.onGetCurrentLanguageSuccess(it)
        }
    }

    override fun setLanguage(langCode: String) {
        languageRepository.setLanguage(langCode).apply {
            view?.onSetLanguageSuccess()
        }
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun setView(view: SettingContract.View?) {
        this.view = view
    }
}
