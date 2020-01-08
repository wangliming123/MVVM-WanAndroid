package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.common.HotKey
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {
    var queryKey: String = ""
    val repository by lazy { SearchRepository(this) }

    val hotKeyList = MutableLiveData<List<HotKey>>()

    private val pageSize = MutableLiveData<Int>()

    private val listing = Transformations.map(pageSize) {
        repository.getListing(it)
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun getHotKey() {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = repository.getHotKey()
                    executeResponse(result, {
                        result.data?.let {
                            hotKeyList.value = it
                        }
                    }, {})
                }
            )

        }
    }


    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun query(queryKey: String, pageSize: Int = 10) {
        this.queryKey = queryKey
        this.pageSize.value = pageSize
    }

}