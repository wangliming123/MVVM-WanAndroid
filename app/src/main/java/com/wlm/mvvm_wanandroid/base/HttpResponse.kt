package com.wlm.mvvm_wanandroid.base

/**
 * 网络请求通用response
 */
data class HttpResponse<T>(val errorCode: Int, val errorMsg: String?, val data: T?)