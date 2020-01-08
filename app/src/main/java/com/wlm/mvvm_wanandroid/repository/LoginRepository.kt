package com.wlm.mvvm_wanandroid.repository

import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.common.User
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository : BaseRepository(){

    suspend fun login(username: String, password: String): HttpResponse<User> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.login(username, password)
        }
    }

    suspend fun register(username: String, password: String, rePassword: String): HttpResponse<User> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.register(username, password, rePassword)
        }
    }

    suspend fun logout(): HttpResponse<Any> {
        return withContext(Dispatchers.Default) {
            RetrofitManager.service.logout()
        }
    }
}