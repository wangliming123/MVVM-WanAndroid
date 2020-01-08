package com.wlm.mvvm_wanandroid.datasource.todo

import com.wlm.mvvm_wanandroid.base.paging.BaseDataSourceFactory
import com.wlm.mvvm_wanandroid.common.Todo
import com.wlm.mvvm_wanandroid.viewmodel.TodoViewModel

class TodoDataSourceFactory(private val viewModel: TodoViewModel) : BaseDataSourceFactory<TodoDataSource, Todo>() {
    override fun createDataSource(): TodoDataSource = TodoDataSource(viewModel)

}