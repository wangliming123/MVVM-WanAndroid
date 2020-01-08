package com.wlm.mvvm_wanandroid.datasource.knowledge

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.ui.fragment.KnowledgeFragment
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeViewModel
import kotlinx.coroutines.launch

class KnowledgeDataSource(private val viewModel: KnowledgeViewModel) :
    ItemKeyedDataSource<Int, Article>() {
    private var page = when (viewModel.type) {
        //项目和公众号api页码从1开始
        KnowledgeFragment.TYPE_PROJECT -> 1
        KnowledgeFragment.TYPE_WX_ARTICLE -> 1
        else -> 0
    }
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
                        val result = when (type) {
                            KnowledgeFragment.TYPE_PROJECT ->
                                repository.getProjectItem(page, knowledgeId)
                            KnowledgeFragment.TYPE_WX_ARTICLE ->
                                repository.getWxArticles(page, knowledgeId)
                            else -> repository.searchArticles(page, knowledgeId)
                        }
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
                tryCatch({
                    val result = when (type) {
                        KnowledgeFragment.TYPE_PROJECT -> repository.getProjectItem(
                            page,
                            knowledgeId
                        )
                        KnowledgeFragment.TYPE_WX_ARTICLE -> repository.getWxArticles(
                            page,
                            knowledgeId
                        )
                        else -> repository.searchArticles(page, knowledgeId)
                    }

                    executeResponse(result, {
                        result.data?.let {
                            page++
                            callback.onResult(it.datas)
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