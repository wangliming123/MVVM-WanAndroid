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
import com.wlm.mvvm_wanandroid.repository.KnowledgeRepository
import com.wlm.mvvm_wanandroid.ui.fragment.KnowledgeFragment
import kotlinx.coroutines.launch

class KnowledgeViewModel : BaseViewModel() {

    var knowledgeId : Int = 0
    var type : Int = KnowledgeFragment.TYPE_KNOWLEDGE
    private val collectRepository by lazy { CollectRepository() }

    val repository by lazy { KnowledgeRepository(this) }

    private val pageSize = MutableLiveData<Int>()
    private val listing = Transformations.map(pageSize) {
        repository.getList(it)
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun collect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = collectRepository.collect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
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

    fun unCollect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = collectRepository.unCollect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
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