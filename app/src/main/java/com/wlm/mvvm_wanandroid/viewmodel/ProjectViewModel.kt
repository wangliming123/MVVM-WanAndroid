package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel : BaseViewModel() {

    val repository by lazy { ProjectRepository() }

    val projects = MutableLiveData<List<Knowledge>>()

    val uiState = MutableLiveData<UiState<List<Knowledge>>>()

    fun getProjectTree() {
        viewModelScope.launch {
            uiState.value = UiState(true, null, null)
            tryCatch(
                tryBlock = {
                    val result = repository.getProjectTree()
                    executeResponse(result, {
                        result.data?.let {
                            uiState.value = UiState(false, null, it)
                            projects.value = it
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