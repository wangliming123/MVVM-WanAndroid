package com.wlm.mvvm_wanandroid.ui.activity

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.DefaultArticleAdapter
import com.wlm.mvvm_wanandroid.base.ui.BaseVMActivity
import com.wlm.mvvm_wanandroid.common.HotKey
import com.wlm.mvvm_wanandroid.viewmodel.SearchViewModel
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseVMActivity<SearchViewModel>() {
    override val layoutId: Int = R.layout.activity_search
    override val providerVMClass: Class<SearchViewModel> = SearchViewModel::class.java
    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private var queryKey: String = ""
    private var hasQuery = false

    private val adapter by lazy { DefaultArticleAdapter() }

    override fun init() {
        super.init()
        initActionBar()

        initSearch()

        mViewModel.getHotKey()

        rv_refresh.adapter = adapter

        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }

    }

    private fun initActionBar() {
        search_toolbar.run {
            setSupportActionBar(search_toolbar)
            search_toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            search_toolbar.setNavigationOnClickListener {
                if (hasQuery) {
                    multiple_status_view.visibility = View.GONE
                    sv_hot_key.visibility = View.VISIBLE
                    hasQuery = false
                } else {
                    onBackPressed()
                }
            }
        }
    }

    private fun initSearch() {

        search_view.run {
            isIconified = false
            onActionViewExpanded()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        queryKey = it
                        queryArticles()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false

            })
        }
    }

    private fun queryArticles() {
        search_view.clearFocus()
        hasQuery = true
        multiple_status_view.visibility = View.VISIBLE
        sv_hot_key.visibility = View.GONE
        mViewModel.query(queryKey)
    }

    override fun startObserve() {
        super.startObserve()

        mViewModel.run {
            hotKeyList.observe(this@SearchActivity, Observer {
                bindHotKey(it)
            })
            pagedList.observe(this@SearchActivity, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@SearchActivity, Observer { state ->
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
                    } else {
                        multipleStatusView?.showContent()
                    }
                }

                state.error?.let {
                    multipleStatusView?.showError()
                    Logger.d("load_error", it)
                }
            })

        }

    }

    private fun bindHotKey(hotKeyList: List<HotKey>) {
        hot_key_tag.run {
            adapter = object : TagAdapter<HotKey>(hotKeyList) {
                override fun getView(parent: FlowLayout?, position: Int, hotKey: HotKey?): View {
                    val tvHotKey = layoutInflater.inflate(
                        R.layout.item_hot_key,
                        parent, false
                    ) as TextView
                    hotKey?.let {
                        tvHotKey.text = it.name
                    }
                    return tvHotKey
                }
            }
            setOnTagClickListener { _, position, _ ->
                queryKey = hotKeyList[position].name
                queryArticles()
                true
            }
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }
}
