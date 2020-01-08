package com.wlm.mvvm_wanandroid.datasource.search

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchDataSource(private val viewModel: SearchViewModel) :
    ItemKeyedDataSource<Int, Article>() {

    private var page = 0
    private var pageCount = 0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Article>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = repository.queryArticles(page, queryKey)
                        executeResponse(result, {
                            result.data?.let {
                                pageCount = it.pageCount
                                page++
                                uiState.value = UiState(false, null, it)
                                callback.onResult(it.datas)
                            }
                        }, { msg ->
                            uiState.value = UiState(false, msg, null)
                        })
                    },
                    catchBlock = { t ->
                        uiState.value = UiState(false, t.message, null)

                    },
                    handleCancellationExceptionManually = true
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        if (page > pageCount) return
        viewModel.run {
            viewModelScope.launch {
                tryCatch(
                    tryBlock = {
                        val result = repository.queryArticles(page, queryKey)
                        executeResponse(result, {
                            result.data?.let {
                                page++
                                callback.onResult(it.datas)
                            }
                        }, {})
                    },
                    handleCancellationExceptionManually = true
                )
            }

        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {
    }

    override fun getKey(item: Article): Int = item.id

}