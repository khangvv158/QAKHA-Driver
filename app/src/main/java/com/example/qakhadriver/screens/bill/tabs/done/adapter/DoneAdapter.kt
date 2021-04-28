package com.example.qakhadriver.screens.bill.tabs.done.adapter

import android.view.ViewGroup
import com.example.qakhadriver.data.model.OrderDone
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.DoneItem
import com.example.qakhadriver.widget.recyclerview.viewholder.DoneViewHolder

class DoneAdapter : CustomRecyclerView.Adapter<DoneViewHolder>(arrayListOf()) {

    private var listener: DoneRecyclerViewOnClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomRecyclerView.ViewHolder<*> {
        return DoneViewHolder(parent).apply {
            registerItemViewHolderListener {
                getItemPosition<DoneItem>(it)?.orderDone?.let { orderDone ->
                    listener?.onItemClickListener(orderDone)
                }
            }
        }
    }

    fun updateData(ordersDone: MutableList<OrderDone>) {
        mItems.clear()
        ordersDone.reverse()
        addItems(ordersDone.map {
            DoneItem(it)
        })
        notifyDataSetChanged()
    }

    fun registerRecyclerViewListener(listener: DoneRecyclerViewOnClickListener) {
        this.listener = listener
    }
}
