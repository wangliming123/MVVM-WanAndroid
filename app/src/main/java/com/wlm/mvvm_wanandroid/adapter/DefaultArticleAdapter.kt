package com.wlm.mvvm_wanandroid.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Article

class DefaultArticleAdapter(private val articleType: Int = TYPE_AUTHOR) : PagedListAdapter<Article, ArticleViewHolder>(diffCallback){
    companion object {
        const val TYPE_AUTHOR = 0
        const val TYPE_SHARE = 1

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem

        }
    }

    private var listener : OnItemChildClickListener<Article>? = null

    fun setOnItemChildClickListener(listener: OnItemChildClickListener<Article>) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(parent)

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position), articleType)
        holder.itemView.findViewById<ImageView>(R.id.iv_collect).setOnClickListener {
            getItem(position)?.run {
                listener?.onClick(it, this)
            }
        }
    }


}