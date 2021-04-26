package com.example.qakhadriver.screens.main

import com.example.qakhadriver.utils.BasePresenter

interface MainActivityContract {

    interface View {

        fun onSetupLanguageSuccess(langCode: String)
    }

    interface Presenter : BasePresenter<View> {

        fun onSetupLanguage()
    }
}
