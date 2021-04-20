package com.example.qakhadriver.widget.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.DoneItem

class DoneViewHolder(viewGroup: ViewGroup) :
    CustomRecyclerView.ViewHolder<DoneItem>(newInstance(viewGroup)) {

    override fun bind(item: DoneItem) {
        with(itemView) {
            //no-op
        }
    }

    companion object {

        fun newInstance(viewGroup: ViewGroup): View {
            return LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout_done, viewGroup, false)
        }
    }
}
