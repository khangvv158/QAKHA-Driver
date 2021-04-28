package com.example.qakhadriver.widget.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.CommentItem
import kotlinx.android.synthetic.main.item_layout_comment.view.*

class CommentViewHolder(viewGroup: ViewGroup) :
    CustomRecyclerView.ViewHolder<CommentItem>(newInstance(viewGroup)) {

    override fun bind(item: CommentItem) {
        with(itemView) {
            idOrderTextView.text = item.comment.idOrder.toString()
            nameUserTextView.text = item.comment.user.name
            ratingBar.rating = item.comment.point.toFloat()
            contentTextView.text = item.comment.content
            timeFeedbackTextView.text = item.comment.timeFeedback
        }
    }

    companion object {

        fun newInstance(viewGroup: ViewGroup): View {
            return LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout_comment, viewGroup, false)
        }
    }
}
