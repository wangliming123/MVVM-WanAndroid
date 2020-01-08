package com.wlm.mvvm_wanandroid.ui.activity

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.*
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.layout_header.*

class BrowserActivity : BaseActivity() {

    companion object {
        const val TAG = "BrowserActivity"
        const val KEY_URL = "url"
    }

    override val layoutId = R.layout.activity_browser

    override fun init() {
        headerToolbar.title = getString(R.string.str_is_loading)
        headerToolbar.setNavigationIcon(R.drawable.arrow_back)
        headerToolbar.setNavigationOnClickListener { onBackPressed() }
        progressBar.progressDrawable = resources.getDrawable(R.drawable.color_progressbar)
        initWebView()

        loadWebView()
    }

    private fun initWebView() {
        webView.run {
            webViewClient = object : WebViewClient() {
                //防止加载网页时调起系统浏览器
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                    Log.e(TAG, "onPageStarted")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                    Log.e(TAG, "onPageFinished")
                }
            }

            webChromeClient = object : WebChromeClient() {

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progressBar.progress = newProgress
                    Log.e(TAG, newProgress.toString())
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    Log.e(TAG, "onReceivedTitle")
                    title?.let { headerToolbar.title = it }
                }
            }
        }
    }

    private fun loadWebView() {
        intent?.extras?.getString(KEY_URL).let {
            Log.d(TAG, "URL: $it")
            webView.loadUrl(it)
//            webView.loadUrl("https://www.baidu.com")
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

}
