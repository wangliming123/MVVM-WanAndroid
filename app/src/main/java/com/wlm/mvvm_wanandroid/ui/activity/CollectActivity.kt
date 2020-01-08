package com.wlm.mvvm_wanandroid.ui.activity

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.DefaultArticleAdapter
import com.wlm.mvvm_wanandroid.adapter.OnItemChildClickListener
import com.wlm.mvvm_wanandroid.base.ui.BaseVMActivity
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.viewmodel.CollectViewModel
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class CollectActivity : BaseVMActivity<CollectViewModel>() {
    override val providerVMClass: Class<CollectViewModel> = CollectViewModel::class.java
    override val layoutId: Int = R.layout.activity_collect
    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private val adapter by lazy { DefaultArticleAdapter() }
    private var isRefreshFromUnCollect = false

    override fun init() {
        super.init()
        toolbar.title = getString(R.string.str_collect)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        refresh_collect.setColorSchemeColors(Color.GREEN, Color.BLUE)
        refresh_collect.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_collect.adapter = adapter
        adapter.setOnItemChildClickListener(object : OnItemChildClickListener<Article> {
            override fun onClick(view: View, data: Article) {
                when(view.id) {
                    R.id.iv_collect -> {
                        mViewModel.unCollect(data.originId)
                    }
                }
            }

        })
    }

    override fun startObserve() {
        super.startObserve()

        mViewModel.run {
            pagedList.observe(this@CollectActivity, Observer {
                adapter.submitList(it)
            })
            uiState.observe(this@CollectActivity, Observer { state ->
                if (!isRefreshFromUnCollect) {
                    refresh_collect.isRefreshing = state.loading
                    if (state.loading) {
                        if (isRefreshFromPull) {
                            isRefreshFromPull = false
                        } else {
                            multipleStatusView?.showLoading()
                        }
                    }

                    state.success?.let {
                        if (it.datas.isEmpty()) {
                            multipleStatusView?.showEmpty()
                        } else {
                            multipleStatusView?.showContent()
                        }
                    }

                    state.error?.let {
                        multipleStatusView?.showError()
                        Logger.d("load_error", it)
                    }
                }else {
                    isRefreshFromUnCollect = true
                }
            })

            unCollectId.observe(this@CollectActivity, Observer {
                isRefreshFromUnCollect = true
                refresh()
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

}
