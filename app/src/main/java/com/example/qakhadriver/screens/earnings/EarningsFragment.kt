package com.example.qakhadriver.screens.earnings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.screens.earnings.adapter.IncomePagerAdapter
import com.example.qakhadriver.screens.earnings.tabs.day.IncomeDayFragment
import com.example.qakhadriver.screens.earnings.tabs.month.IncomeMonthFragment
import com.example.qakhadriver.screens.earnings.tabs.year.IncomeYearFragment
import kotlinx.android.synthetic.main.fragment_earnings.*

class EarningsFragment : Fragment() {

    private val incomePagerAdapter by lazy {
        IncomePagerAdapter(childFragmentManager, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_earnings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        incomeViewPager.apply {
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
            adapter = incomePagerAdapter.apply {
                addFragment(IncomeDayFragment.newInstance())
                addFragment(IncomeMonthFragment.newInstance())
                addFragment(IncomeYearFragment.newInstance())
            }
        }
        tabLayout.setupWithViewPager(incomeViewPager)
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"
        private const val OFF_SCREEN_PAGE_LIMIT = 1

        fun newInstance(driver: Driver) = EarningsFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
