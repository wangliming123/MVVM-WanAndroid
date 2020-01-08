package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.datasource.collect.CollectDataSourceFactory
import com.wlm.mvvm_wanandroid.viewmodel.CollectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectRepository() : BaseRepository() {
    private var viewModel: CollectViewModel? = null

    constructor(viewModel: CollectViewModel) : this() {
        this.viewModel = viewModel
    }

    private val sourceFactory by lazy {
        viewModel?.let {
            CollectDataSourceFactory(it)
        }
    }

    fun getListing(pageSize: Int): Listing<Article>? {
        sourceFactory?.let {
            val pagedList = LivePagedListBuilder(
                it, PagedList.Config.Builder()
                    .setEnablePlaceholders(true)
                    .setPageSize(pageSize)
                    .setInitialLoadSizeHint(pageSize * 2)
                    .build()
            ).build()
            return Listing(pagedList, refresh = { it.sourceLivaData.value?.invalidate() })
        }
        return null
    }

    suspend fun collect(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.collect(id)
        }
    }

    suspend fun unCollect(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.unCollect(id)
        }
    }

    suspend fun getCollectArticles(page: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getCollectArticles(page)
        }
    }

}