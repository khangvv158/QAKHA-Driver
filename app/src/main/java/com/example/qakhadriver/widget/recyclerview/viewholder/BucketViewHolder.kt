package com.example.qakhadriver.widget.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.utils.loadUrl
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.BucketItem
import kotlinx.android.synthetic.main.item_layout_bucket.view.*

class BucketViewHolder(viewGroup: ViewGroup) :
    CustomRecyclerView.ViewHolder<BucketItem>(newInstance(viewGroup)) {

    override fun bind(item: BucketItem) {
        with(itemView) {
            imageViewProduct?.loadUrl(item.cart.product.image.imageUrl)
            textViewNameProduct?.text = item.cart.product.name
            textViewQuantityProduct?.text = item.cart.quantity.toString()
            textViewPriceProduct?.text = item.cart.price.toString()
        }
    }

    companion object {

        fun newInstance(viewGroup: ViewGroup): View {
            return LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout_bucket, viewGroup, false)
        }
    }
}
