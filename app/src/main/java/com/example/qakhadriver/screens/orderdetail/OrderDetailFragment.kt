package com.example.qakhadriver.screens.orderdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.orderdetail.adapter.BucketAdapter
import com.example.qakhadriver.utils.IPositiveNegativeListener
import com.example.qakhadriver.utils.showDialogWithListener
import kotlinx.android.synthetic.main.fragment_order_detail.*

class OrderDetailFragment : Fragment() {

    private val bucketAdapter by lazy {
        BucketAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    private fun initViews() {
        recyclerViewBucket.adapter = bucketAdapter
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        doneButton.setOnClickListener {
            requireContext().showDialogWithListener(
                getString(R.string.delivery_confirmation),
                getString(R.string.make_sure_your_order_has_been_delivered_correctly),
                object : IPositiveNegativeListener {

                    override fun onPositive() {
                        //no-op
                    }
                },
                getString(R.string.done),
                true
            )
        }
    }

    companion object {
        fun newInstance() = OrderDetailFragment()
    }
}
