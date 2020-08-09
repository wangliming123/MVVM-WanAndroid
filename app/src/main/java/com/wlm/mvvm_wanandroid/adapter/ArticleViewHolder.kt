package com.wlm.mvvm_wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.BrowserActivity

class ArticleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
) {
    private var article: Article? = null
    private val context = parent.context

    private val tvAuthor = itemView.findViewById<TextView>(R.id.tv_author)
    private val tvTop = itemView.findViewById<TextView>(R.id.tv_top)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
    private val tvDesc = itemView.findViewById<TextView>(R.id.tv_desc)
    private val tvChapter = itemView.findViewById<TextView>(R.id.tv_chapter)
    private val ivPic = itemView.findViewById<ImageView>(R.id.iv_pic)
    private val ivCollect = itemView.findViewById<ImageView>(R.id.iv_collect)

    fun bind(article: Article?, articleType: Int = DefaultArticleAdapter.TYPE_AUTHOR) {
        this.article = article
        article?.run {
            if (articleType == DefaultArticleAdapter.TYPE_SHARE) {
                tvAuthor.text = shareUser
            } else {
                tvAuthor.text = author
            }
            tvTop.visibility = if (isTop) View.VISIBLE else View.GONE
            tvDate.text = niceDate
            tvTitle.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            if (desc.isNullOrBlank()) tvDesc.visibility = View.GONE
            else tvDesc.text = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvChapter.text = when {
                (superChapterName != null && superChapterName.isNotBlank()) and chapterName.isNotBlank() ->
                    "${superChapterName}/ $chapterName"
                (superChapterName != null && superChapterName.isNotBlank()) -> superChapterName
                chapterName.isNotBlank() -> chapterName
                else -> ""
            }
            if (envelopePic == null || envelopePic.isBlank()) {
                ivPic.visibility = View.GONE
            } else {
                ivPic.visibility = View.VISIBLE
                Glide.with(context).load(envelopePic).into(ivPic)
            }
            ivCollect.setImageDrawable(ResourcesCompat.getDrawable(context.resources,
                if (collect) R.drawable.ic_like else R.drawable.ic_like_normal, null))
            itemView.setOnClickListener {
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to link)
            }
        }
    }

}