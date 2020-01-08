package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.WxArticleRepository
import kotlinx.coroutines.launch

class WxArticleViewModel : BaseViewModel() {
    private val repository by lazy { WxArticleRepository() }

    val wxList = MutableLiveData<List<Knowledge>>()

    val uiState = MutableLiveData<UiState<List<Knowledge>>>()

    fun getWxList() {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = repository.getWxList()
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, it)
                            wxList.value = it
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