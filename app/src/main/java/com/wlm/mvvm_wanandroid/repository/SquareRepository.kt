package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.datasource.square.SquareDataSourceFactory
import com.wlm.mvvm_wanandroid.viewmodel.SquareViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SquareRepository(private val viewModel: SquareViewModel) : BaseRepository() {
    private val sourceFactory by lazy { SquareDataSourceFactory(viewModel) }


    fun getListingData(pageSize: Int): Listing<Article> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.sourceLivaData.value?.invalidate() })
    }

    suspend fun getSquareArticle(page: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getSquareArticle(page)
        }
    }

    suspend fun shareArticle(title: String, link: String): HttpResponse<Any> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.shareArticle(title, link)
        }
    }

}