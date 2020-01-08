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
import com.wlm.mvvm_wanandroid.viewmodel.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseVMFragment<ProjectViewModel>() {
    override val layoutId = R.layout.fragment_project

    override val providerVMClass: Class<ProjectViewModel> = ProjectViewModel::class.java

    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private val fragments = mutableListOf<Fragment>()

    override fun init() {
        super.init()

        mViewModel.getProjectTree()

    }

    private fun initViewPager(projects: List<Knowledge>) {
        fragments.clear()
        projects.forEach {
            fragments.add(KnowledgeFragment(it, KnowledgeFragment.TYPE_PROJECT))
        }
        project_view_pager.adapter = object : FragmentPagerAdapter(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? =
                HtmlCompat.fromHtml(projects[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun initTabLayout() {
        project_tab_layout.run {
            setupWithViewPager(project_view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        project_view_pager.currentItem = it.position
                    }
                }

            })
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            projects.observe(this@ProjectFragment, Observer {
                initViewPager(it)
                initTabLayout()
            })

            uiState.observe(this@ProjectFragment, Observer { state ->

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

}