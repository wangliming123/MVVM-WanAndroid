package com.wlm.mvvm_wanandroid.viewmodel

import androidx.lifecycle.viewModelScope
import com.wlm.mvvm_wanandroid.base.BaseViewModel
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.utils.SharedPrefs
import com.wlm.mvvm_wanandroid.executeResponse
import com.wlm.mvvm_wanandroid.repository.LoginRepository
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    private val loginRepository by lazy { LoginRepository() }
    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    fun logout() {
        viewModelScope.launch {
            tryCatch(
                tryBlock = {
                    val result = loginRepository.logout()
                    executeResponse(result, {
                        isLogin = false
                    }, {})
                }
            )
        }
    }

}