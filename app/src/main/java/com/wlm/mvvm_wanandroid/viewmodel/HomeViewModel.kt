package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.base.UiState
import com.wlm.mvvm_wanandroid.common.ArticleList
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.utils.SharedPrefs
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.CollectRepository
import com.wlm.mvvm_wanandroid.repository.HomeRepository
import com.wlm.mvvm_wanandroid.repository.LoginRepository
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    val homeRepository by lazy { HomeRepository(this) }
    private val collectRepository by lazy { CollectRepository() }

    private val loginRepository by lazy { LoginRepository() }

    val loginState = MutableLiveData<Boolean>()

    private val pageSize = MutableLiveData<Int>()

    private val result = Transformations.map(pageSize) {
        homeRepository.getListingData(it)
    }

    val pagedList = Transformations.switchMap(result) { it.pagedList }


    val uiState = MutableLiveData<UiState<ArticleList>>()

    fun initLoad(pageSize: Int = 10) {
        if (this.pageSize.value != pageSize) this.pageSize.value = pageSize
    }

    fun refresh() {
        result.value?.refresh?.invoke()
    }

    fun login() {
        val userString by SharedPrefs(Constant.USER_STRING, "")
        if (userString.isEmpty()) {
            loginState.value = false
        }
        val namePassword = userString.split(",")
        val username = namePassword[0]
        val password = namePassword[1]
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = loginRepository.login(username, password)
                    executeResponse(result, {
                        loginState.value = true
                    }, {
                        loginState.value = false
                    })
                },
                catchBlock = {
                    loginState.value = false
                }
            )
        }
    }

    fun collect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = collectRepository.collect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
                    }, {
                        Logger.d(result.toString())
                    })
                },
                catchBlock = { t ->
                    Logger.d(t.message)
                }
            )
        }

    }

    fun unCollect(id: Int) {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = collectRepository.unCollect(id)
                    executeResponse(result, {
                        Logger.d(result.toString())
                    }, {
                        Logger.d(result.toString())
                    })
                },
                catchBlock = { t ->
                    Logger.d(t.message)
                }
            )
        }
    }


}