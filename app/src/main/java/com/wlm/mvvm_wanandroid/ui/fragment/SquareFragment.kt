package com.wlm.mvvm_wanandroid.ui.fragment

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.DefaultArticleAdapter
import com.wlm.mvvm_wanandroid.adapter.OnItemChildClickListener
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.common.Article
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.utils.SharedPrefs
import com.wlm.mvvm_wanandroid.common.utils.ToastUtils
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.LoginActivity
import com.wlm.mvvm_wanandroid.viewmodel.SquareViewModel
import kotlinx.android.synthetic.main.fragment_square.*
import kotlinx.android.synthetic.main.refresh_layout.*


class SquareFragment : BaseVMFragment<SquareViewModel>() {
    override val layoutId = R.layout.fragment_square

    override val providerVMClass: Class<SquareViewModel> = SquareViewModel::class.java

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private val adapter by lazy { DefaultArticleAdapter(DefaultArticleAdapter.TYPE_SHARE) }

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    override fun init() {
        super.init()

        initRecyclerView()
        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }
        initShare()
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

    private fun initShare() {
        fab_add_share.setOnClickListener {
            layout_square_article.visibility = View.GONE
            layout_share.visibility = View.VISIBLE
        }
        btn_share.setOnClickListener {
            val title = et_title.text.toString()
            val link = et_link.text.toString()
            when{
                title.isEmpty() -> {
                    tv_tips.visibility = View.VISIBLE
                    tv_tips.text = getString(R.string.str_tip_title_not_empty)
                }
                link.isEmpty() -> {
                    tv_tips.visibility = View.VISIBLE
                    tv_tips.text = getString(R.string.str_tip_link_not_empty)
                }
                !link.startsWith("http://") && !link.startsWith("https://") -> {
                    tv_tips.visibility = View.VISIBLE
                    tv_tips.text = getString(R.string.str_tip_link_not_valid)
                }
                else -> {
                    tv_tips.visibility = View.GONE
                    mViewModel.share(title, link)
                }
            }
        }
        btn_cancel.setOnClickListener {
            layout_square_article.visibility = View.VISIBLE
            layout_share.visibility = View.GONE
        }
    }


    override fun startObserve() {
        super.startObserve()
        mViewModel.run {

            pagedList.observe(this@SquareFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@SquareFragment, Observer { state ->
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
            shareState.observe(this@SquareFragment, Observer {
                ToastUtils.show(it)
                if ("分享成功" == it) {
                    layout_square_article.visibility = View.VISIBLE
                    layout_share.visibility = View.GONE
                }
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

}