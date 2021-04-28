package com.example.qakhadriver.screens.feedback.adapter

import android.view.ViewGroup
import com.example.qakhadriver.data.model.Comment
import com.example.qakhadriver.widget.recyclerview.CustomRecyclerView
import com.example.qakhadriver.widget.recyclerview.item.CommentItem
import com.example.qakhadriver.widget.recyclerview.viewholder.CommentViewHolder

class CommentAdapter : CustomRecyclerView.Adapter<CommentViewHolder>(arrayListOf()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomRecyclerView.ViewHolder<*> {
        return CommentViewHolder(parent)
    }

    fun updateData(comments: MutableList<Comment>) {
        mItems.clear()
        comments.reverse()
        addItems(comments.map {
            CommentItem(it)
        })
        notifyDataSetChanged()
    }
}
