package com.wlm.mvvm_wanandroid

import com.wlm.mvvm_wanandroid.base.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

suspend fun<T> executeResponse(
    response: HttpResponse<T>,
    successBlock: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend CoroutineScope.(String?) -> Unit
) {
    coroutineScope {
        if (response.errorCode == -1) errorBlock(response.errorMsg)
        else successBlock()
    }
}

