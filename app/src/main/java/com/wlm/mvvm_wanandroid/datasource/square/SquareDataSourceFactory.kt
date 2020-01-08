package com.wlm.mvvm_wanandroid.datasource.square

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.viewmodel.SquareViewModel

class SquareDataSourceFactory(private val viewModel: SquareViewModel)  : BaseDataSourceFactory<SquareDataSource, Article>() {
    override fun createDataSource(): SquareDataSource = SquareDataSource(viewModel)

}