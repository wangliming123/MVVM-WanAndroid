package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.repository.KnowledgeTreeRepository

class KnowledgeTreeViewModel : BaseViewModel() {
    val knowledgeTreeRepository by lazy { KnowledgeTreeRepository(this) }

    private val pageSize = MutableLiveData<Int>()

    private val listing = Transformations.map(pageSize) {
        knowledgeTreeRepository.getList(it)
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<List<Knowledge>>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

}