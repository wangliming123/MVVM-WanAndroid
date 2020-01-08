package com.wlm.mvvm_wanandroid.datasource.knowledgeTree

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeTreeViewModel
import kotlinx.coroutines.launch

class KnowledgeTreeDataSource(private val viewModel: KnowledgeTreeViewModel) :
    ItemKeyedDataSource<Int, Knowledge>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Knowledge>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = knowledgeTreeRepository.getKnowledgeTree()
                        executeResponse(result, {
                            if (result.data != null) {
                                uiState.value = UiState(false, null, result.data)
                                callback.onResult(result.data)
                            }
                        }, { msg ->
                            uiState.value = UiState(false, msg, null)
                        })
                    },
                    catchBlock = { t->
                        uiState.value = UiState(false, t.message, null)
                    },
                    handleCancellationExceptionManually = true
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Knowledge>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Knowledge>) {

    }

    override fun getKey(item: Knowledge): Int = item.id

}