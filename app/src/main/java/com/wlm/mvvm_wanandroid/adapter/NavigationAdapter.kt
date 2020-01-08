package com.wlm.mvvm_wanandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Navigation

class NavigationAdapter(private val listener: OnItemClickListener) :
    PagedListAdapter<Navigation, NavigationViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Navigation>() {
            override fun areItemsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem.cid == newItem.cid

            override fun areContentsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }


    private var positionChecked = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        return NavigationViewHolder(parent)
    }

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {
            setChecked(position)
            listener.onItemClicked(position)
        }
        holder.tvNavigation.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (positionChecked == position) R.color.colorPrimary else R.color.textColorSecondary
            )
        )
    }

    fun setChecked(position: Int) {
        positionChecked = position
        notifyDataSetChanged()

    }
}


class NavigationViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_navigation, parent, false)
) {
    val tvNavigation: TextView = itemView.findViewById(R.id.tv_navigation)
    fun bind(navigation: Navigation?) {
        tvNavigation.text = navigation?.name
    }
}