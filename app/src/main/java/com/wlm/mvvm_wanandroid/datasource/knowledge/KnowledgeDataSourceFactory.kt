package com.wlm.mvvm_wanandroid.datasource.knowledge

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeViewModel

class KnowledgeDataSourceFactory(private val viewModel: KnowledgeViewModel) :
    BaseDataSourceFactory<KnowledgeDataSource, Article>() {
    override fun createDataSource(): KnowledgeDataSource = KnowledgeDataSource(viewModel)

}