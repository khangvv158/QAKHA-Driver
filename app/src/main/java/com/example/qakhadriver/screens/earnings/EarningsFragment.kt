package com.example.qakhadriver.screens.earnings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver

class EarningsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_earnings, container, false)
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"

        fun newInstance(driver: Driver) = EarningsFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
