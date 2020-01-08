package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Navigation
import com.wlm.mvvm_wanandroid.repository.NavigationRepository

class NavigationViewModel : BaseViewModel() {
    val repository by lazy { NavigationRepository(this) }

    private val pageSize = MutableLiveData<Int>()

    private val listing = Transformations.map(pageSize) {
        repository.getList(it)
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<List<Navigation>>>()


    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }
}