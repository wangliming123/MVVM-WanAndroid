package com.wlm.mvvm_wanandroid.datasource.search

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.viewmodel.SearchViewModel

class SearchDataSourceFactory(private val viewModel: SearchViewModel) : BaseDataSourceFactory<SearchDataSource, Article>() {
    override fun createDataSource(): SearchDataSource = SearchDataSource(viewModel)

}