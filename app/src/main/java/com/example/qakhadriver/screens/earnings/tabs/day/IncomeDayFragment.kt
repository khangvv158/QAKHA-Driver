package com.example.qakhadriver.screens.earnings.tabs.day

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.IncomeRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.response.IncomeDayResponse
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.utils.TimeHelper
import com.example.qakhadriver.utils.makeText
import com.example.qakhadriver.utils.toString
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_income_day.*
import java.text.SimpleDateFormat
import java.util.*

class IncomeDayFragment : Fragment(), DatePickerDialog.OnDateSetListener, IncomeDayContract.View {

    private val presenter by lazy {
        IncomeDayPresenter(
            IncomeRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_income_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvent()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = monthOfYear + 1
        val date = SimpleDateFormat(
            Constants.DATE_PATTERN,
            Locale.getDefault()
        ).parse("$dayOfMonth-$month-$year")
        dayTextView.text = date.toString(Constants.DATE_PATTERN)
        presenter.getIncomeDay(date.toString(Constants.DATE_PATTERN))
    }

    override fun onGetIncomeDaySuccess(incomeDayResponse: IncomeDayResponse) {
        totalCashTextView.text = incomeDayResponse.totalCash.toString()
        totalCoinTextView.text = incomeDayResponse.totalCoin.toString()
        totalPayPalTextView.text = incomeDayResponse.totalPayPal.toString()
        totalDailyTextView.text = incomeDayResponse.totalDaily.toString()
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initViews() {
        presenter.setView(this)
        dayTextView.text = TimeHelper.getTimeCurrent(Constants.DATE_PATTERN)
        presenter.getIncomeDay(TimeHelper.getTimeCurrent(Constants.DATE_PATTERN))
    }

    private fun handleEvent() {
        imageViewCalendar.setOnClickListener {
            val now = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog.newInstance(
                this, now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show(parentFragmentManager, DATE_PICKER_DIALOG)
        }
    }

    companion object {

        private const val DATE_PICKER_DIALOG = "DATE_PICKER_DIALOG"

        fun newInstance() = IncomeDayFragment()
    }
}
