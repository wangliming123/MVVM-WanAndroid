package com.wlm.mvvm_wanandroid.datasource.navigation

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemKeyedDataSource
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Navigation
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

class NavigationDataSource(private val viewModel: NavigationViewModel) : ItemKeyedDataSource<Int, Navigation>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Navigation>
    ) {
        viewModel.run {
            viewModelScope.launch {
                uiState.value = UiState(true, null, null)
                tryCatch(
                    tryBlock = {
                        val result = repository.getNavigation()

                        executeResponse(result, {
                            result.data?.let {
                                uiState.value = UiState(false, null, it)
                                callback.onResult(it)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Navigation>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Navigation>) {

    }

    override fun getKey(item: Navigation): Int = item.cid

}