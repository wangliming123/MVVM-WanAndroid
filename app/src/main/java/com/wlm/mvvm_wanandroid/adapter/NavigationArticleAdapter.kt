package com.wlm.mvvm_wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.Navigation
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.BrowserActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

class NavigationArticleAdapter :
    PagedListAdapter<Navigation, NavigationArticleViewHolder>(diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Navigation>() {
            override fun areItemsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem.cid == newItem.cid

            override fun areContentsTheSame(oldItem: Navigation, newItem: Navigation): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationArticleViewHolder {
        return NavigationArticleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: NavigationArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NavigationArticleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.layout_navigation_article, parent, false)
) {

    private val tvName = itemView.findViewById<TextView>(R.id.tv_navigation_name)
    private val flowLayout = itemView.findViewById<TagFlowLayout>(R.id.flow_layout)

    fun bind(navigation: Navigation?) {
        navigation?.run {
            tvName.text = name
            flowLayout.run {
                adapter = object : TagAdapter<Article>(articles) {
                    override fun getView(parent: FlowLayout, position: Int, article: Article?): View {
                        val tvArticle = LayoutInflater.from(parent.context).inflate(
                            R.layout.item_navigation_article, parent, false
                        ) as TextView
                        article?.let {
                            tvArticle.text = it.title
                        }
                        return tvArticle
                    }

                }

                setOnTagClickListener { _, position, parent ->
                    val article = articles[position]
                    parent.context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to article.link)

                    true
                }
            }
        }
    }

}