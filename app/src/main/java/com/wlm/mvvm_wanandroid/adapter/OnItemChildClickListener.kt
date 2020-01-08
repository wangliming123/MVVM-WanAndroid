package com.wlm.mvvm_wanandroid.adapter

import android.view.View

interface OnItemChildClickListener<T> {
    fun onClick(view: View, data: T)
}