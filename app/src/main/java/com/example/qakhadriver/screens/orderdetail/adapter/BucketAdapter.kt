package com.example.qakhadriver.screens.orderdetail.adapter

import android.view.ViewGroup
import com.example.qakhadriver.data.model.Cart
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.BucketItem
import com.example.qakhadriver.widget.recyclerview.viewholder.BucketViewHolder

class BucketAdapter : CustomRecyclerView.Adapter<BucketViewHolder>(arrayListOf()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomRecyclerView.ViewHolder<*> {
        return BucketViewHolder(parent)
    }

    fun updateData(carts: MutableList<Cart>) {
        mItems.clear()
        addItems(carts.map {
            BucketItem(it)
        })
    }
}
