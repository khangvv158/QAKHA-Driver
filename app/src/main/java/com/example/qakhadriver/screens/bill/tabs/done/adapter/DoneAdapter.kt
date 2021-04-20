package com.example.qakhadriver.screens.bill.tabs.done.adapter

import android.view.ViewGroup
import com.example.qakhadriver.data.model.Order
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.DoneItem
import com.example.qakhadriver.widget.recyclerview.viewholder.DoneViewHolder

class DoneAdapter : CustomRecyclerView.Adapter<DoneViewHolder>(arrayListOf()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomRecyclerView.ViewHolder<*> {
        return DoneViewHolder(parent)
    }

    fun updateData(orders: MutableList<Order>) {
        mItems.clear()
        addItems(orders.map {
            DoneItem(it)
        })
    }
}
