package com.example.qakhadriver.screens.earnings.tabs.month

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.IncomeRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.response.IncomeMonthResponse
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.utils.TimeHelper
import com.example.qakhadriver.utils.makeText
import com.example.qakhadriver.utils.toString
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import kotlinx.android.synthetic.main.fragment_income_month.*
import kotlinx.android.synthetic.main.fragment_income_month.imageViewCalendar
import java.text.SimpleDateFormat
import java.util.*

class IncomeMonthFragment : Fragment(), IncomeMonthContract.View {

    private val presenter by lazy {
        IncomeMonthPresenter(
            IncomeRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_income_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onGetIncomeMonthSuccess(incomeMonthResponse: IncomeMonthResponse) {
        totalCashTextView?.text = incomeMonthResponse.totalCash.toString()
        totalCoinTextView?.text = incomeMonthResponse.totalCoin.toString()
        totalPayPalTextView?.text = incomeMonthResponse.totalPayPal.toString()
        totalMonthTextView?.text = incomeMonthResponse.totalMonthly.toString()
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initViews() {
        presenter.setView(this)
        dateTextView?.text = TimeHelper.getTimeCurrent(Constants.MONTH_YEAR)
        presenter.getIncomeMonth(TimeHelper.getTimeCurrent(Constants.MONTH_YEAR))
    }

    private fun handleEvents() {
        imageViewCalendar.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dialog = MonthYearPickerDialogFragment.getInstance(
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
            )
            dialog.show(parentFragmentManager, null)
            dialog.setOnDateSetListener { year, monthOfYear ->
                val month = monthOfYear + 1
                val date = SimpleDateFormat(
                    Constants.MONTH_YEAR,
                    Locale.getDefault()
                ).parse("$month-$year")
                dateTextView?.text = date.toString(Constants.MONTH_YEAR)
                presenter.getIncomeMonth(date.toString(Constants.MONTH_YEAR))
            }
        }
    }

    companion object {
        fun newInstance() = IncomeMonthFragment()
    }
}
