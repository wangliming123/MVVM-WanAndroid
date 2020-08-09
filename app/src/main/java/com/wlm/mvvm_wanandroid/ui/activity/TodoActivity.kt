package com.wlm.mvvm_wanandroid.ui.activity

import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseActivity
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.ui.fragment.OnParentRefreshListener
import com.wlm.mvvm_wanandroid.ui.fragment.TodoFragment
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class TodoActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_todo

    private var titles = arrayOf(R.string.str_todo, R.string.str_finished)

    private val todoFragment by lazy { TodoFragment() }
    private val finishedTodoFragment by lazy { TodoFragment(Constant.TODO_STATUS_FINISHED, false) }
    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(todoFragment)
        fragmentList.add(finishedTodoFragment)
    }

    override fun init() {
        initToolBar()
        initViewPager()
        initTabLayout()
        initFragment()
    }

    private fun initToolBar() {
        toolbar.run {
            title = getString(R.string.str_todo_list)
            setSupportActionBar(this)
            navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun initViewPager() {
        todo_view_pager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment = fragmentList[position]

            override fun getCount(): Int = fragmentList.size

            override fun getPageTitle(position: Int): CharSequence? = getString(titles[position])

        }
    }

    private fun initTabLayout() {
        todo_tab_layout.setupWithViewPager(todo_view_pager)
        todo_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    todo_view_pager.currentItem = it.position
                }
            }

        })
    }

    private val onParentRefreshListener = object : OnParentRefreshListener {
        override fun onRefresh() {
            todoFragment.refreshWithNotLoading()
            finishedTodoFragment.refreshWithNotLoading()
        }
    }


    private fun initFragment() {
        todoFragment.listener = onParentRefreshListener
        finishedTodoFragment.listener = onParentRefreshListener
    }
}
