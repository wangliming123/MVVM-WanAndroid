package com.wlm.mvvm_wanandroid.datasource.collect

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.viewmodel.CollectViewModel

class CollectDataSourceFactory(private val viewModel: CollectViewModel) : BaseDataSourceFactory<CollectDataSource, Article>() {
    override fun createDataSource(): CollectDataSource =
        CollectDataSource(viewModel)

}