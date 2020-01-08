package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.datasource.knowledgeTree.KnowledgeTreeDataSourceFactory
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeTreeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KnowledgeTreeRepository(private val viewModel: KnowledgeTreeViewModel) : BaseRepository() {


    private val sourceFactory by lazy { KnowledgeTreeDataSourceFactory(viewModel) }

    fun getList(pageSize: Int): Listing<Knowledge> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()
        ).build()
        return Listing(pagedList, refresh = {sourceFactory.sourceLivaData.value?.invalidate()})
    }


    suspend fun getKnowledgeTree(): HttpResponse<List<Knowledge>> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getKnowledgeTree()
        }
    }


}