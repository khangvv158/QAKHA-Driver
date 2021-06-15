package com.example.qakhadriver.screens.donedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.data.model.OrderDone
import com.example.qakhadriver.screens.orderdetail.OrderDetailFragment
import com.example.qakhadriver.screens.orderdetail.adapter.BucketAdapter
import kotlinx.android.synthetic.main.fragment_done_detail.*
import kotlinx.android.synthetic.main.fragment_done_detail.imageViewBack
import kotlinx.android.synthetic.main.fragment_done_detail.textViewAddress
import kotlinx.android.synthetic.main.fragment_done_detail.textViewDateOrder
import kotlinx.android.synthetic.main.fragment_done_detail.textViewNamePartner
import kotlinx.android.synthetic.main.fragment_done_detail.textViewPhoneNumber
import kotlinx.android.synthetic.main.fragment_done_detail.textViewPriceDiscount
import kotlinx.android.synthetic.main.fragment_done_detail.textViewPriceShippingFee
import kotlinx.android.synthetic.main.fragment_done_detail.textViewPriceSubtotal
import kotlinx.android.synthetic.main.fragment_done_detail.textViewPriceTotal
import kotlinx.android.synthetic.main.fragment_done_detail.textViewUserName

class DoneDetailFragment : Fragment() {

    private val bucketAdapter by lazy {
        BucketAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_done_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
        handleEvents()
    }

    private fun initViews() {
        recyclerViewBucket.adapter = bucketAdapter
    }

    private fun initData() {
        arguments?.getParcelable<OrderDone>(BUNDLE_DONE)?.let {
            textViewAddress.text = it.addressUser
            textViewDateOrder.text = it.createdAt
            textViewUserName.text = it.nameUser
            textViewPhoneNumber.text = it.phoneNumberUser
            textViewNamePartner.text = it.partner.name
            bucketAdapter.updateData(it.carts)
            textViewPriceSubtotal.text = it.subtotal.toString()
            textViewPriceShippingFee.text = it.shippingFee.toString()
            textViewPriceDiscount.text = it.discount.toString()
            textViewPriceTotal.text = it.total.toString()
            handleEventCall(it)
        }
    }

    private fun handleEventCall(orderDone: OrderDone) {
        imageViewCall.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:${orderDone.phoneNumberUser}")
                )
            )
        }
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {

        private const val BUNDLE_DONE = "BUNDLE_DONE"

        fun newInstance(orderDone: OrderDone) = DoneDetailFragment().apply {
            arguments = bundleOf(BUNDLE_DONE to orderDone)
        }
    }
}
