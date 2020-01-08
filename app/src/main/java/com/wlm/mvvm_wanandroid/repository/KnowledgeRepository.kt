package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.datasource.knowledge.KnowledgeDataSourceFactory
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KnowledgeRepository(private val viewModel: KnowledgeViewModel) : BaseRepository() {

    private val sourceFactory by lazy { KnowledgeDataSourceFactory(viewModel) }

    fun getList(pageSize: Int): Listing<Article> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .build()
        ).build()
        return Listing(pagedList, refresh = {sourceFactory.sourceLivaData.value?.invalidate()})
    }

    suspend fun searchArticles(page: Int, knowledgeId: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getKnowledgeItem(page, knowledgeId)
        }
    }
    suspend fun getProjectItem(page: Int, knowledgeId: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getProjectItem(page, knowledgeId)
        }
    }

    suspend fun getWxArticles(page: Int, knowledgeId: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getWxArticles(knowledgeId, page)
        }
    }
}