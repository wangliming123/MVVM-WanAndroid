package com.wlm.mvvm_wanandroid.ui.activity

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.NavigationAdapter
import com.wlm.mvvm_wanandroid.adapter.NavigationArticleAdapter
import com.wlm.mvvm_wanandroid.base.ui.BaseVMActivity
import com.wlm.mvvm_wanandroid.viewmodel.NavigationViewModel
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class NavigationActivity : BaseVMActivity<NavigationViewModel>() {

    override val layoutId: Int = R.layout.activity_navigation

    override val providerVMClass: Class<NavigationViewModel> = NavigationViewModel::class.java
    private var checkedPosition: Int = 0
    private val adapterNavigation by lazy {
        NavigationAdapter(object : NavigationAdapter.OnItemClickListener {
            override fun onItemClicked(position: Int) {
                checkedPosition = position
                rv_navigation_article.scrollToPosition(position)
            }

        })
    }

    private val adapterNavigationArticle by lazy { NavigationArticleAdapter() }

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    override fun init() {
        super.init()

        toolbar.title = getString(R.string.str_navigation)
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = getDrawable(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener { finish() }

        initRecycler()

        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
            adapterNavigation.setChecked(0)
        }
    }

    private fun initRecycler() {
        rv_navigation.adapter = adapterNavigation
        rv_navigation_article.adapter = adapterNavigationArticle
        rv_navigation_article.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val index = (recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstVisibleItemPosition()
                    if (index != checkedPosition) {
                        rv_navigation.scrollToPosition(index)
                        adapterNavigation.setChecked(index)
                        checkedPosition = index
                    }
                }
            }
        })
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            pagedList.observe(this@NavigationActivity, Observer {
                adapterNavigation.submitList(it)
                adapterNavigationArticle.submitList(it)
            })

            uiState.observe(this@NavigationActivity, Observer { state ->
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
                    }else {
                        multipleStatusView?.showContent()
                    }
                }

                state.error?.let {
                    multipleStatusView?.showError()
                    Logger.d("load_error", it)
                }
            })

            initLoad()
        }

    }

    override fun retry() {
        mViewModel.refresh()
    }

}
