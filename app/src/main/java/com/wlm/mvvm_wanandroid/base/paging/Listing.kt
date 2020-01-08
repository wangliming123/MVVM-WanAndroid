package com.wlm.mvvm_wanandroid.base.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val refresh: () -> Unit //刷新
)