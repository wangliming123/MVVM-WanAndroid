package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.common.BannerData
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.datasource.home.HomeDataSourceFactory
import com.wlm.mvvm_wanandroid.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository(private val homeViewModel: HomeViewModel) : BaseRepository() {

    private val sourceFactory by lazy { HomeDataSourceFactory(homeViewModel) }

    fun getListingData(pageSize: Int): Listing<Article> {
        val pageList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pageList, refresh = { sourceFactory.sourceLivaData.value?.invalidate() })
    }

    suspend fun getArticleList(page: Int): HttpResponse<ArticleList> {
        return withContext(Dispatchers.IO) { apiCall { RetrofitManager.service.getArticles(page) } }

    }

    suspend fun getBanners(): HttpResponse<List<BannerData>> {
        return withContext(Dispatchers.IO) { apiCall { RetrofitManager.service.getBanners() } }
    }

    suspend fun getTop(): HttpResponse<List<Article>> {
        return withContext(Dispatchers.IO) { apiCall { RetrofitManager.service.getTop() } }
    }
}