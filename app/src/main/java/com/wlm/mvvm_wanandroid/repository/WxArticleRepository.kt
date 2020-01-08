package com.wlm.mvvm_wanandroid.repository

import com.wlm.mvvm_wanandroid.base.BaseRepository
import com.wlm.mvvm_wanandroid.base.HttpResponse
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WxArticleRepository : BaseRepository() {
    suspend fun getWxList() : HttpResponse<List<Knowledge>> {
        return withContext(Dispatchers.IO) {
            RetrofitManager.service.getWxList()
        }
    }

}