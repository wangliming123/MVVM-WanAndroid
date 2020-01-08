package com.wlm.mvvm_wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.Todo

class TodoAdapter(private val status: Int = Constant.TODO_STATUS_TODO) :
    PagedListAdapter<Todo, TodoViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
                oldItem == newItem

        }
    }

    private var listener : OnItemChildClickListener<Todo>? = null
    fun setOnItemChildClickListener(listener: OnItemChildClickListener<Todo>) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
        TodoViewHolder(parent, status)


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.findViewById<ImageView>(R.id.iv_finish_or_revert).setOnClickListener { view ->
            getItem(position)?.let {
                listener?.onClick(view, it)
            }
        }
        holder.itemView.findViewById<ImageView>(R.id.iv_delete).setOnClickListener { view ->
            getItem(position)?.let {
                listener?.onClick(view, it)
            }
        }
        holder.itemView.setOnClickListener { view ->
            getItem(position)?.let {
                listener?.onClick(view, it)
            }
        }
    }


}


class TodoViewHolder(parent: ViewGroup, private val status: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_todo_title)
    private val tvContent = itemView.findViewById<TextView>(R.id.tv_todo_content)
    private val tvDataTime = itemView.findViewById<TextView>(R.id.tv_todo_date_time)
    private val tvCompleteTime = itemView.findViewById<TextView>(R.id.tv_todo_complete_time)
    private val ivFinishOrRevert = itemView.findViewById<ImageView>(R.id.iv_finish_or_revert)
    fun bind(todo: Todo?) {
        if (status == Constant.TODO_STATUS_FINISHED) {
            ivFinishOrRevert.setImageResource(R.mipmap.ic_revert)
            tvDataTime.visibility = View.GONE
        } else {
            ivFinishOrRevert.setImageResource(R.mipmap.ic_finish)
            tvCompleteTime.visibility = View.GONE
        }
        todo?.run {
            tvTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvContent.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvDataTime.text = tvCompleteTime.context.getString(R.string.str_date_time, dateStr)
            tvCompleteTime.text = tvCompleteTime.context.getString(R.string.str_complete_time, completeDateStr)
        }
    }

}