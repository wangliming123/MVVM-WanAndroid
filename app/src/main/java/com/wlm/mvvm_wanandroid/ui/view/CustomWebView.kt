package com.wlm.mvvm_wanandroid.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.res.ResourcesCompat
import com.wlm.mvvm_wanandroid.R


class CustomWebView : WebView {

    private val client = object : WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    constructor(arg0: Context) : super(arg0) {

        setBackgroundColor(ResourcesCompat.getColor(arg0.resources, R.color.color_bg, null))
    }

    constructor(arg0: Context, arg1: AttributeSet) : super(arg0, arg1) {
        webViewClient = client
        initWebSetting()
        isClickable = true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebSetting() {
        settings.run {
            //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
            javaScriptEnabled = true

            //自适应屏幕
            useWideViewPort = true//将图片调整到适合webview的大小
            loadWithOverviewMode = true//缩放至屏幕大小

            //缩放操作
            setSupportZoom(true)
            builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
            displayZoomControls = false//隐藏原生的缩放控件


            allowFileAccess = true//设置可以访问文件
            javaScriptCanOpenWindowsAutomatically = true//支持通过JS打开新窗口
            cacheMode = WebSettings.LOAD_NO_CACHE //关闭webview中缓存
            defaultTextEncodingName = "utf-8"//设置编码格式
            loadsImagesAutomatically = true//支持自动加载图片

//            setSupportMultipleWindows(true)
//            setAppCacheEnabled(true)
//            domStorageEnabled = true
//            setGeolocationEnabled(true)
        }
    }

}
