package com.wlm.mvvm_wanandroid.ui.activity

import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseVMActivity
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.utils.SharedPrefs
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.fragment.*
import com.wlm.mvvm_wanandroid.common.utils.ToastUtils
import com.wlm.mvvm_wanandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseVMActivity<MainViewModel>() {
    override val layoutId: Int = R.layout.activity_main
    override val providerVMClass: Class<MainViewModel> = MainViewModel::class.java

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)

    private val bottomTitles = arrayOf(
        R.string.str_home,
        R.string.str_square,
        R.string.str_knowledge_tree,
        R.string.str_project,
        R.string.str_wx_article
//        R.string.str_navigation
    )

    private val fragmentList = arrayListOf<Fragment>()
    private val homeFragment by lazy { HomeFragment() }
    private val squareFragment by lazy { SquareFragment() }
    private val knowledgeTreeFragment by lazy { KnowledgeTreeFragment() }
    private val projectFragment by lazy { ProjectFragment() }
    private val wxArticleFragment by lazy { WxArticleFragment() }
//    private val navigationItemView by lazy { NavigationFragment() }

    init {
        fragmentList.add(homeFragment)
        fragmentList.add(squareFragment)
        fragmentList.add(knowledgeTreeFragment)
        fragmentList.add(projectFragment)
        fragmentList.add(wxArticleFragment)
//        fragmentList.add(navigationItemView)
    }

    override fun init() {
        super.init()
        setSupportActionBar(toolbar)
        initDrawerLayout()
        initNav()
        initViewPager()
        initNavBottom()
    }

    private fun initDrawerLayout() {
        drawer_layout.run {
            val toggle = object : ActionBarDrawerToggle(
                this@MainActivity,
                this,
                toolbar,
                R.string.app_name,
                R.string.app_name
            ) {
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    nav_view.menu.findItem(R.id.logout).isVisible = isLogin
                }
            }
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initNav() {
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    if (isLogin) {
                        startKtxActivity<CollectActivity>()
                    } else {
                        startKtxActivity<LoginActivity>()
                    }
                }
                R.id.todo -> {
                    if (isLogin) {
                        startKtxActivity<TodoActivity>()
                    } else {
                        startKtxActivity<LoginActivity>()
                    }
                }
//                R.id.night_mode -> {
//                }
//                R.id.setting -> {
//                }
                R.id.logout -> {
                    mViewModel.logout()
                }
                R.id.about -> {
                    AlertDialog.Builder(this).setTitle(R.string.app_name)
                        .setMessage(
                            getString(
                                R.string.str_source_code,
                                "https://github.com/wangliming123/MVVM-WanAndroid"
                            )
                        )
                        .setPositiveButton(R.string.str_confirm, null)
                        .create()
                        .show()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initViewPager() {
        view_pager.run {
            offscreenPageLimit = bottomTitles.size
            adapter = object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getItem(position: Int) = fragmentList[position]

                override fun getCount() = fragmentList.size

                override fun getPageTitle(position: Int) = getString(bottomTitles[position])

            }
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    bottom_navigation.selectedItemId =
                        bottom_navigation.menu.getItem(position).itemId
                    toolbar.title =
                        getString(if (position == 0) R.string.app_name else bottomTitles[position])
                }

            })

        }
    }

    private fun initNavBottom() {
        bottom_navigation.run {
            labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> view_pager.currentItem = 0
                    R.id.square -> view_pager.currentItem = 1
                    R.id.knowledge_tree -> view_pager.currentItem = 2
                    R.id.project -> view_pager.currentItem = 3
                    R.id.wx_article -> view_pager.currentItem = 4
//                    R.id.navigation -> view_pager.currentItem = 5
                }
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation -> {
                startKtxActivity<NavigationActivity>()
            }
            R.id.search -> {
                startKtxActivity<SearchActivity>()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var lastExitTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (KeyEvent.KEYCODE_BACK == keyCode) {
            val current = System.currentTimeMillis()
            if (current - lastExitTime > 2000) {
                ToastUtils.show(getString(R.string.str_exit_hint))
                lastExitTime = current
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {

        }
    }
}
