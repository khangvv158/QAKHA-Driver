package com.example.qakhadriver.screens.earnings.tabs.month

import com.example.qakhadriver.data.source.remote.schema.response.IncomeMonthResponse
import com.example.qakhadriver.utils.BasePresenter

interface IncomeMonthContract {

    interface View {

        fun onGetIncomeMonthSuccess(incomeMonthResponse: IncomeMonthResponse)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getIncomeMonth(date: String)
    }
}
