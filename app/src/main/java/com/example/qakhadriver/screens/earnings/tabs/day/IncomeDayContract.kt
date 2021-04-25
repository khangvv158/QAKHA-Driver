package com.example.qakhadriver.screens.earnings.tabs.day

import com.example.qakhadriver.data.source.remote.schema.response.IncomeDayResponse
import com.example.qakhadriver.utils.BasePresenter

interface IncomeDayContract {

    interface View {

        fun onGetIncomeDaySuccess(incomeDayResponse: IncomeDayResponse)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getIncomeDay(day: String)
    }
}
