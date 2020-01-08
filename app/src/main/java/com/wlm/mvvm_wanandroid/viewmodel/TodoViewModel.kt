package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.Todo
import com.wlm.mvvm_wanandroid.common.TodoList
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel : BaseViewModel() {
    val repository by lazy { TodoRepository(this) }

    var status = Constant.TODO_STATUS_TODO

    private val pageSize = MutableLiveData<Int>()

    private val listing = Transformations.map(pageSize) {
        repository.getListingData(it)
    }

    val pagedList = Transformations.switchMap(listing) {
        it.pagedList
    }

    val uiState = MutableLiveData<UiState<TodoList>>()

    val todoState = MutableLiveData<UiState<Int>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }


    fun finishTodo(todo: Todo, status: Int) {
        val successState =
            if (status == Constant.TODO_STATUS_TODO) Constant.TODO_FINISH else Constant.TODO_REVERT
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = repository.finishTodo(todo.id, status)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, successState)
                    }, { msg ->
                        todoState.value = UiState(false, msg, successState)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, successState)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = repository.deleteTodo(todo.id)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, Constant.TODO_DELETE)
                    }, { msg ->
                        todoState.value = UiState(false, msg, Constant.TODO_DELETE)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, Constant.TODO_DELETE)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun addTodo(title: String, content: String, dateStr: String) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = repository.addTodo(title, content, dateStr)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, Constant.TODO_ADD)
                    }, { msg ->
                        todoState.value = UiState(false, msg, Constant.TODO_ADD)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, Constant.TODO_ADD)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

    fun updateTodo(id: Int, title: String, content: String, dateStr: String, status: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = repository.updateTodo(id, title, content, dateStr, status)
                    executeResponse(result, {
                        todoState.value = UiState(true, null, Constant.TODO_UPDATE)
                    }, { msg ->
                        todoState.value = UiState(false, msg, Constant.TODO_UPDATE)
                    })
                },
                catchBlock = { t ->
                    todoState.value = UiState(false, t.message, Constant.TODO_UPDATE)
                },
                handleCancellationExceptionManually = true
            )
        }
    }

}