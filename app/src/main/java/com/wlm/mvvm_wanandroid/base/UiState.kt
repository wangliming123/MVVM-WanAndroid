package com.wlm.mvvm_wanandroid.base

data class UiState<T>(
    val loading: Boolean,
    val error: String?,
    val success: T?
)