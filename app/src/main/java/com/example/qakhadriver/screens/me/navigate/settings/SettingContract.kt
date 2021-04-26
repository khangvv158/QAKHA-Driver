package com.example.qakhadriver.screens.me.navigate.settings

import com.example.qakhadriver.utils.BasePresenter

interface SettingContract {

    interface View {

        fun onGetCurrentLanguageSuccess(langCode: String)
        fun onSetLanguageSuccess()
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getCurrentLanguage()
        fun setLanguage(langCode: String)
    }
}
