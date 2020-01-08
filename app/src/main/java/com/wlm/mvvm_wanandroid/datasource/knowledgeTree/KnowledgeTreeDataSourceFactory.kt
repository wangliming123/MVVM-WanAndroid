package com.wlm.mvvm_wanandroid.datasource.knowledgeTree

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeTreeViewModel

class KnowledgeTreeDataSourceFactory(private val viewModel: KnowledgeTreeViewModel) : BaseDataSourceFactory<KnowledgeTreeDataSource, Knowledge>() {
    override fun createDataSource(): KnowledgeTreeDataSource = KnowledgeTreeDataSource(viewModel)

}