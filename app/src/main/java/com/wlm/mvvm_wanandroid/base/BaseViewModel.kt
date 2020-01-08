package com.wlm.mvvm_wanandroid.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * ViewModel 基类
 */
open class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    val mException: MutableLiveData<Throwable> = MutableLiveData()

    /**
     * @param handleCancellationExceptionManually 是否修改Exception值（是否调用BaseVMFragment(Activity)的onError方法）
     */
    suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {},
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                catchBlock(e)
                if (handleCancellationExceptionManually) {
                    mException.value = e
                }
            } finally {
                finallyBlock()
            }
        }
    }
}