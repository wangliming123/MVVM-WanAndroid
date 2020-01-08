package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.CollectRepository
import kotlinx.coroutines.launch

class CollectViewModel : BaseViewModel() {

    val collectRepository by lazy { CollectRepository(this) }

    private val pageSize = MutableLiveData<Int>()

    private val listing = Transformations.map(pageSize) {
        collectRepository.getListing(it)!!
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    val unCollectId = MutableLiveData<Int>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun unCollect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = collectRepository.unCollect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
                        unCollectId.value = id
                    }, {
                        Logger.d(result.toString())
                    })
                },
                catchBlock = { t ->
                    Logger.d(t.message)
                }
            )
        }
    }

}