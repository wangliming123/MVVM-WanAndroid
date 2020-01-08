package com.wlm.mvvm_wanandroid.ui.activity

import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseActivity
import com.wlm.mvvm_wanandroid.common.Knowledge
import com.wlm.mvvm_wanandroid.ui.fragment.KnowledgeFragment
import kotlinx.android.synthetic.main.activity_knowledge.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class KnowledgeActivity : BaseActivity() {

    companion object {
        const val KEY_KNOWLEDGE = "KEY_KNOWLEDGE"
    }


    override val layoutId: Int = R.layout.activity_knowledge

    private val knowledge by lazy { intent?.extras?.get(KEY_KNOWLEDGE) as Knowledge }

    private val fragments = mutableListOf<Fragment>()


    override fun init() {
        setSupportActionBar(toolbar)

        toolbar.title = knowledge.name
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        initViewPager()

        initTabLayout()
    }

    private fun initViewPager() {
        fragments.clear()
        knowledge.children.forEach {
            fragments.add(KnowledgeFragment(it))
        }
        knowledge_view_pager.run {
            adapter = object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getItem(position: Int): Fragment = fragments[position]

                override fun getCount(): Int = fragments.size

                override fun getPageTitle(position: Int): CharSequence? =
                    HtmlCompat.fromHtml(
                        knowledge.children[position].name,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )


            }
        }
    }

    private fun initTabLayout() {
        knowledge_tab_layout.run {
            setupWithViewPager(knowledge_view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        knowledge_view_pager.currentItem = it.position
                    }
                }

            })
        }
    }

}
