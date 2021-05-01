package com.example.qakhadriver.screens.orderdetail

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
import com.example.qakhadriver.data.repository.OrderRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.orderdetail.adapter.BucketAdapter
import com.example.qakhadriver.utils.IPositiveNegativeListener
import com.example.qakhadriver.utils.makeText
import com.example.qakhadriver.utils.showDialogWithListener
import kotlinx.android.synthetic.main.fragment_order_detail.*

class OrderDetailFragment : Fragment(), OrderDetailContract.View {

    private val bucketAdapter by lazy {
        BucketAdapter()
    }
    private val presenter by lazy {
        OrderDetailPresenter(
            OrderRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
            )
        )
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
        initData()
    }

    override fun completeDeliverySuccess() {
        makeText(getString(R.string.complete))
        parentFragmentManager.popBackStack()
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initViews() {
        presenter.setView(this)
        recyclerViewBucket.adapter = bucketAdapter
    }

    private fun initData() {
        arguments?.getParcelable<Order>(BUNDLE_DRIVER)?.let {
            textViewAddress.text = it.orderDetail.addressUser
            textViewDateOrder.text = it.orderDetail.createdAt
            textViewUserName.text = it.orderDetail.nameUser
            textViewPhoneNumber.text = it.orderDetail.phoneNumberUser
            textViewNamePartner.text = it.orderDetail.partner.name
            bucketAdapter.updateData(it.carts)
            textViewPriceSubtotal.text = it.orderDetail.subtotal.toString()
            textViewPriceShippingFee.text = it.orderDetail.shippingFee.toString()
            textViewPriceDiscount.text = it.orderDetail.discount.toString()
            textViewPriceTotal.text = it.orderDetail.total.toString()
            handleEvents(it)
        }
    }

    private fun handleEvents(order: Order) {
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        doneButton.setOnClickListener {
            requireContext().showDialogWithListener(
                getString(R.string.delivery_confirmation),
                getString(R.string.make_sure_your_order_has_been_delivered_correctly),
                object : IPositiveNegativeListener {

                    override fun onPositive() {
                        presenter.completeDelivery(
                            order.orderDetail.id,
                            order.orderDetail.driverId
                        )
                    }
                },
                getString(R.string.done),
                true
            )
        }
        imageViewCall.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:${order.orderDetail.phoneNumberUser}")
                )
            )
        }
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"

        fun newInstance(order: Order) = OrderDetailFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to order)
        }
    }
}
