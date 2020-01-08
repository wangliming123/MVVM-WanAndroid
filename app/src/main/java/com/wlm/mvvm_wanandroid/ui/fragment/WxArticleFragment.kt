package com.wlm.mvvm_wanandroid.ui.fragment

import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.viewmodel.WxArticleViewModel
import kotlinx.android.synthetic.main.fragment_wx_article.*

class WxArticleFragment : BaseVMFragment<WxArticleViewModel>() {
    override val layoutId = R.layout.fragment_wx_article
    override val providerVMClass: Class<WxArticleViewModel> = WxArticleViewModel::class.java
    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private val fragments = mutableListOf<Fragment>()
    override fun init() {
        super.init()

        mViewModel.getWxList()

    }

    override fun startObserve() {
        super.startObserve()

        mViewModel.run {
            wxList.observe(this@WxArticleFragment, Observer {
                initViewPager(it)
                initTabLayout()
            })

            uiState.observe(this@WxArticleFragment, Observer { state ->

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
                    Logger.d("load_error", it)
                }
            })
        }
    }

    private fun initViewPager(wxList: List<Knowledge>) {
        fragments.clear()
        wxList.forEach {
            fragments.add(KnowledgeFragment(it, KnowledgeFragment.TYPE_WX_ARTICLE))
        }
        wx_article_view_pager.adapter = object : FragmentPagerAdapter(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? =
                HtmlCompat.fromHtml(wxList[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initTabLayout() {
        wx_article_tab_layout.run {
            setupWithViewPager(wx_article_view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        wx_article_view_pager.currentItem = it.position
                    }
                }
            })
        }
    }

}