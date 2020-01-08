package com.wlm.mvvm_wanandroid.datasource.home

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.viewmodel.HomeViewModel
import kotlinx.coroutines.*

class HomeDataSource(private val homeViewModel: HomeViewModel) :
    ItemKeyedDataSource<Int, Article>() {

    private var page = 0
    private var pageCount = 0
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        homeViewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        //同步获取banner和article
//                        val bannerJob = async { homeRepository.getBanners() }
//                        val articleJob = async { homeRepository.getArticleList(page) }
//
//                        val bannerResult = bannerJob.await()
//                        val result = articleJob.await()
                        val result = homeRepository.getArticleList(page)
                        val topResult = homeRepository.getTop()
                        val bannerResult = homeRepository.getBanners()
                        executeResponse(result, {
                            result.data?.let { articleList ->
                                pageCount = articleList.pageCount
                                page++

                                executeResponse(topResult, {
                                    topResult.data?.let {
                                        it.forEach { top ->
                                            top.isTop = true
                                        }
                                        articleList.datas.addAll(0, it)
                                    }
                                }, {})
                                //将banner数据放入article数据的的第一条(article.size + 1)
                                executeResponse(bannerResult, {
                                    val article = articleList.datas[0].copy()
                                    article.bannerList = bannerResult.data
                                    articleList.datas.add(0, article)
                                }, {})
                                homeViewModel.uiState.value = UiState(false, null, result.data)
                                callback.onResult(result.data.datas)
                            }

                        }, { msg ->
                            homeViewModel.uiState.value = UiState(false, msg, null)
                        })
                    },
                    catchBlock = { t ->
                        homeViewModel.uiState.value = UiState(false, t.message, null)
                    },
                    handleCancellationExceptionManually = true
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        if (page > pageCount) return
        homeViewModel.run {
            viewModelScope.launch {
                tryCatch({
                    val result = homeRepository.getArticleList(page)
                    executeResponse(result, {
                        result.data?.let {
                            page++
                            callback.onResult(result.data.datas)
                        }
                    }, {})
                }, handleCancellationExceptionManually = true)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {

    }

    override fun getKey(item: Article): Int = item.id

}