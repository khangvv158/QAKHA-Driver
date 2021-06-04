package com.example.qakhadriver.screens.earnings.tabs.year

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.IncomeRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.response.IncomeYearResponse
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.utils.TimeHelper
import com.example.qakhadriver.utils.makeText
import kotlinx.android.synthetic.main.dialog_number_picker.view.*
import kotlinx.android.synthetic.main.fragment_income_year.*
import kotlinx.android.synthetic.main.fragment_income_year.dateTextView
import kotlinx.android.synthetic.main.fragment_income_year.imageViewCalendar
import kotlinx.android.synthetic.main.fragment_income_year.totalCashTextView
import kotlinx.android.synthetic.main.fragment_income_year.totalCoinTextView
import kotlinx.android.synthetic.main.fragment_income_year.totalPayPalTextView


class IncomeYearFragment : Fragment(), IncomeYearContract.View {

    private val presenter by lazy {
        IncomeYearPresenter(
            IncomeRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }
    private var dateChoose: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_income_year, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onGetIncomeYearSuccess(incomeYearResponse: IncomeYearResponse) {
        swipeLayout.isRefreshing = false
        totalCashTextView?.text = incomeYearResponse.totalCash.toString()
        totalCoinTextView?.text = incomeYearResponse.totalCoin.toString()
        totalPayPalTextView?.text = incomeYearResponse.totalPayPal.toString()
        totalAnnualTextView?.text = incomeYearResponse.totalAnnual.toString()
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initViews() {
        presenter.setView(this)
        dateTextView?.text = TimeHelper.getTimeCurrent(Constants.YEAR_PATTERN)
        presenter.getIncomeYear(TimeHelper.getTimeCurrent(Constants.YEAR_PATTERN))
        dateChoose = TimeHelper.getTimeCurrent(Constants.YEAR_PATTERN)
    }

    private fun handleEvents() {
        imageViewCalendar.setOnClickListener {
            showPickerDialog()
        }
        swipeLayout.setOnRefreshListener {
            dateChoose?.let {
                presenter.getIncomeYear(it)
            }
        }
    }

    private fun showPickerDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val view: View = this.layoutInflater.inflate(R.layout.dialog_number_picker, null)
        builder.setView(view)
        builder.setTitle(getString(R.string.choose_year));
        view.picker.minValue = MIN_YEAR
        view.picker.maxValue = MAX_YEAR
        view.picker.value = TimeHelper.getTimeCurrent(Constants.YEAR_PATTERN).toInt()
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            presenter.getIncomeYear(view.picker.value.toString().trim())
            dateChoose = view.picker.value.toString().trim()
            dateTextView?.text = view.picker.value.toString().trim()
        }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    companion object {

        private const val MIN_YEAR = 2015
        private const val MAX_YEAR = 2045

        fun newInstance() = IncomeYearFragment()
    }
}
