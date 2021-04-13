package com.example.qakhadriver.widget.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.NoneItem

class NoneViewHolder(viewGroup: ViewGroup) :
    CustomRecyclerView.ViewHolder<NoneItem>(newInstance(viewGroup)) {

    override fun bind(item: NoneItem) = Unit

    companion object {
        fun newInstance(viewGroup: ViewGroup): View {
            return LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout_none_view, viewGroup, false)
        }
    }
}
