package com.wlm.mvvm_wanandroid.datasource.home

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.viewmodel.HomeViewModel

class HomeDataSourceFactory(private val homeViewModel: HomeViewModel) : BaseDataSourceFactory<HomeDataSource, Article>() {

    override fun createDataSource(): HomeDataSource =
        HomeDataSource(homeViewModel)

}