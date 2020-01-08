package com.wlm.mvvm_wanandroid.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.base.paging.Listing
import com.wlm.mvvm_wanandroid.common.Todo
import com.wlm.mvvm_wanandroid.common.TodoList
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.datasource.todo.TodoDataSourceFactory
import com.wlm.mvvm_wanandroid.viewmodel.TodoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository(private val viewModel: TodoViewModel) : BaseRepository() {
    private val sourceFactory by lazy { TodoDataSourceFactory(viewModel) }

    fun getListingData(pageSize: Int): Listing<Todo> {
        val pagedList = LivePagedListBuilder(
            sourceFactory, PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPageSize(pageSize).build()
        ).build()
        return Listing(pagedList, refresh = { sourceFactory.sourceLivaData.value?.invalidate() })
    }

    suspend fun getTodoList(page: Int, status: Int): HttpResponse<TodoList> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getTodoList(page, status)
        }
    }

    suspend fun finishTodo(id: Int, status: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.finishTodo(id, status)
        }
    }

    suspend fun deleteTodo(id: Int): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.deleteTodo(id)
        }
    }

    suspend fun addTodo(title: String, content: String, dateStr: String): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.addTodo(title, content, dateStr)
        }
    }

    suspend fun updateTodo(id: Int, title: String, content: String, dateStr: String, status: Int): HttpResponse<Any>  {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.updateTodo(id, title, content, dateStr, status)
        }
    }
}