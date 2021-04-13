package com.example.qakhadriver.widget.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.widget.recyclerview.viewholder.NoneViewHolder

class CustomRecyclerView {

    abstract class Item

    abstract class ViewHolder<T : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun superBind(item: Item) {
            val itemBind = item as? T
            if (itemBind != null) {
                bind(item)
            }
        }

        abstract fun bind(item: T)
    }

    abstract class Adapter<T>(items: List<Item>) : RecyclerView.Adapter<ViewHolder<*>>() {

        val mItems = ArrayList<Item>()
        private val mViewTypes = HashMap<String, Int>()

        init {
            mItems.addAll(items)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> {
            return NoneViewHolder(parent)
        }

        override fun getItemCount(): Int = mItems.size

        override fun onBindViewHolder(holder: ViewHolder<*>, position: Int) {
            holder.superBind(mItems[position])
        }

        override fun getItemViewType(position: Int): Int {
            val className = mItems[position].javaClass.name
            if (!mViewTypes.containsKey(className)) {
                mViewTypes[className] = mViewTypes.size
            }
            return mViewTypes[className] ?: Constants.NOT_EXISTS
        }

        open fun getViewType(className: String?): Int {
            val result: Int? = mViewTypes[className]
            return result ?: -1
        }

        fun getClassType(position: Int): Int {
            val nameClass: String = mItems[position].javaClass.name
            return mViewTypes[nameClass] ?: Constants.NOT_EXISTS
        }

        fun <T : Item> insertItem(item: T, position: Int) {
            mItems.add(position, item)
            notifyItemInserted(position)
        }

        fun <T : Item> addItems(items: List<T>) {
            mItems.addAll(items)
            notifyItemRangeInserted(mItems.size - items.size, items.size)
        }

        fun <T : Item> addItem(items: T) {
            mItems.add(items)
            notifyItemInserted(mItems.size - 1)
        }

        fun <T : Item> getItems(): List<T> {
            return mItems.mapNotNull {
                it as? T
            }
        }

        fun <T : Item> updateItem(item: T) {
            val index = mItems.indexOfFirst { it == item }
            if (index != Constants.NOT_EXISTS) {
                notifyItemChanged(index)
            }
        }

        fun <T : Item> replaceItem(item: T, position: Int) {
            mItems[position] = item
            notifyItemChanged(position)
        }

        fun <T : Item> removeItem(item: T) {
            val index = mItems.indexOfFirst { it == item }
            if (index != Constants.NOT_EXISTS) {
                mItems.removeAt(index)
                notifyItemRemoved(index)
            }
        }

        fun <T : Item> getItemPosition(item: T): Int {
            return mItems.indexOf(item)
        }

        fun <T : Item> getItemPosition(position: Int): T? {
            if (mItems.size > 0 && position > -1) {
                return mItems[position] as T
            }
            return null
        }

        fun clearItems() {
            mItems.clear()
            notifyDataSetChanged()
        }
    }
}
