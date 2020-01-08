package com.wlm.mvvm_wanandroid.ui.fragment

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.HomeAdapter
import com.wlm.mvvm_wanandroid.adapter.OnItemChildClickListener
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.LoginActivity
import com.wlm.mvvm_wanandroid.common.utils.SharedPrefs
import com.wlm.mvvm_wanandroid.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.refresh_layout.*

class HomeFragment : BaseVMFragment<HomeViewModel>() {
    override val layoutId = R.layout.refresh_layout
    override val providerVMClass = HomeViewModel::class.java

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private val adapter by lazy { HomeAdapter() }

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    override fun init() {
        super.init()

        initUser()
        initRecyclerView()

        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
    }

    private fun initUser() {
        if (isLogin) {
            mViewModel.login()
        }
    }

    private fun initRecyclerView() {
        rv_refresh.adapter = adapter
        adapter.setOnItemChildClickListener(object : OnItemChildClickListener<Article> {
            override fun onClick(view: View, data: Article) {
                when(view.id) {
                    R.id.iv_collect -> {
                        if (isLogin) {
                            if (data.collect) {
                                data.collect = false
                                mViewModel.unCollect(data.id)
                            }else {
                                data.collect = true
                                mViewModel.collect(data.id)
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            startKtxActivity<LoginActivity>()
                        }
                    }
                }
            }

        })
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@HomeFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@HomeFragment, Observer { state->
                layout_refresh.isRefreshing = state.loading

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
                    }else {
                        multipleStatusView?.showContent()
                    }
                }

                state.error?.let {
                    multipleStatusView?.showError()
                    Logger.d("load_error", it)
                }
            })

            loginState.observe(this@HomeFragment, Observer {
                isLogin = it
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

}