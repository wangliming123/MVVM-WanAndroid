package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Navigation
import com.wlm.mvvm_wanandroid.datasource.navigation.NavigationDataSourceFactory
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.viewmodel.NavigationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NavigationRepository(private val viewModel: NavigationViewModel) : BaseRepository() {
    private val sourceFactory by lazy { NavigationDataSourceFactory(viewModel) }

    fun getList(pageSize: Int): Listing<Navigation> {
        val pagedList = LivePagedListBuilder(
            sourceFactory,
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.sourceLivaData.value?.invalidate() })
    }

    suspend fun getNavigation(): HttpResponse<List<Navigation>> {
        return withContext(Dispatchers.IO) { RetrofitManager.service.getNavigation() }
    }
}