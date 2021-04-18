package com.example.qakhadriver.screens.bill.tabs.doing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R

class DoingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doing, container, false)
    }

    companion object {
        fun newInstance() = DoingFragment()
    }
}
