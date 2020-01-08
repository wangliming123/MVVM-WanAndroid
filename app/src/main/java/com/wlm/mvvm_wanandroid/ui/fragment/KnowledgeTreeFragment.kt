package com.wlm.mvvm_wanandroid.ui.fragment

import android.graphics.Color
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.KnowledgeTreeAdapter
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.viewmodel.KnowledgeTreeViewModel
import kotlinx.android.synthetic.main.refresh_layout.*

class KnowledgeTreeFragment : BaseVMFragment<KnowledgeTreeViewModel>() {
    override val layoutId = R.layout.refresh_layout

    override val providerVMClass: Class<KnowledgeTreeViewModel> = KnowledgeTreeViewModel::class.java

    private val adapter by lazy { KnowledgeTreeAdapter() }

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    override fun init() {
        super.init()

        rv_refresh.adapter = adapter

        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@KnowledgeTreeFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@KnowledgeTreeFragment, Observer { state->
                layout_refresh.isRefreshing = state.loading

                if (state.loading) {
                    if (isRefreshFromPull) {
                        isRefreshFromPull = false
                    } else {
                        multipleStatusView?.showLoading()
                    }
                }

                state.success?.let {
                    if (it.isEmpty()) {
                        multipleStatusView?.showEmpty()
                    } else {
                        multipleStatusView?.showContent()
                    }
                }

                state.error?.let {
                    multipleStatusView?.showError()
                    Logger.d("knowledge tree error", it)
                }
            })

            initLoad()
        }
    }


    override fun retry() {
        mViewModel.refresh()
    }
}