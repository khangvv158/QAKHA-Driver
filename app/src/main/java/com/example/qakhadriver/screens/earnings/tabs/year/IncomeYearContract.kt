package com.example.qakhadriver.screens.earnings.tabs.year

import com.example.qakhadriver.data.source.remote.schema.response.IncomeYearResponse
import com.example.qakhadriver.utils.BasePresenter

interface IncomeYearContract {

    interface View {

        fun onGetIncomeYearSuccess(incomeYearResponse: IncomeYearResponse)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getIncomeYear(year: String)
    }
}
