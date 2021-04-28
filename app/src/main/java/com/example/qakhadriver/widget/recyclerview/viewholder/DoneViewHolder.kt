package com.example.qakhadriver.widget.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.DoneItem
import kotlinx.android.synthetic.main.item_layout_done.view.*

class DoneViewHolder(viewGroup: ViewGroup) :
    CustomRecyclerView.ViewHolder<DoneItem>(newInstance(viewGroup)) {

    override fun bind(item: DoneItem) {
        with(itemView) {
            coinTextView.text = item.orderDone.shippingFee.toString()
            namePartnerTextView.text = item.orderDone.partner.name
            addressPartnerTextView.text = item.orderDone.partner.address
            addressUserTextView.text = item.orderDone.addressUser
            nameUserTextView.text = item.orderDone.nameUser
            payTextView.text = item.orderDone.subtotal.toString()
            rateTextView.text = item.orderDone.rateStatus
            if (item.orderDone.typeCheckout == Constants.CASH) {
                typeCheckoutTextView.text = context.getString(R.string.cash)
            } else {
                typeCheckoutTextView.text = context.getString(R.string.coin)
            }
        }
    }

    fun registerItemViewHolderListener(listener: (Int) -> Unit) {
        itemView.setOnClickListener {
            listener(adapterPosition)
        }
    }

    companion object {

        fun newInstance(viewGroup: ViewGroup): View {
            return LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout_done, viewGroup, false)
        }
    }
}
