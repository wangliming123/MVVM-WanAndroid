package com.wlm.mvvm_wanandroid.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.adapter.OnItemChildClickListener
import com.wlm.mvvm_wanandroid.adapter.TodoAdapter
import com.wlm.mvvm_wanandroid.base.ui.BaseVMFragment
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.Todo
import com.wlm.mvvm_wanandroid.common.utils.ToastUtils
import com.wlm.mvvm_wanandroid.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.refresh_layout.*
import java.text.SimpleDateFormat
import java.util.*

class TodoFragment(private val status: Int = Constant.TODO_STATUS_TODO,
                   private val showAddBtn: Boolean = true) :
    BaseVMFragment<TodoViewModel>() {

    override val providerVMClass: Class<TodoViewModel> = TodoViewModel::class.java
    override val layoutId: Int = R.layout.fragment_todo
    override fun childMultipleStatusView(): MultipleStatusView? = multiple_status_view

    private val adapter by lazy { TodoAdapter(status) }

    private var isRefreshFromUpdateOrDelete = false

    private var isUpdate = false
    private lateinit var todo: Todo

    override fun init() {
        super.init()
        initRecyclerView()
        layout_refresh.setColorSchemeColors(Color.GREEN, Color.BLUE)
        layout_refresh.setOnRefreshListener {
            isRefreshFromPull = true
            mViewModel.refresh()
        }

        initTodo()
    }

    private fun initRecyclerView() {
        rv_refresh.adapter = adapter
        adapter.setOnItemChildClickListener(object : OnItemChildClickListener<Todo> {
            override fun onClick(view: View, data: Todo) {
                when (view.id) {
                    R.id.iv_finish_or_revert -> {
                        mViewModel.finishTodo(data, if (status == Constant.TODO_STATUS_TODO) Constant.TODO_STATUS_FINISHED else Constant.TODO_STATUS_TODO)
                    }
                    R.id.iv_delete -> mViewModel.deleteTodo(data)
                    R.id.view_todo_item -> {
                        layout_add_todo.visibility = View.VISIBLE
                        layout_todo_list.visibility = View.GONE
                        todo = data
                        isUpdate = true
                        et_title.setText(HtmlCompat.fromHtml(todo.title, HtmlCompat.FROM_HTML_MODE_LEGACY))
                        et_content.setText(HtmlCompat.fromHtml(todo.content, HtmlCompat.FROM_HTML_MODE_LEGACY))
                        et_date_time.setText(todo.dateStr)
                    }
                }
            }

        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun initTodo() {
        if (!showAddBtn) fab_add_todo.hide()
        val exampleDateStr = SimpleDateFormat("yyyy-MM-dd").format(Date())
        fab_add_todo.setOnClickListener {
            layout_add_todo.visibility = View.VISIBLE
            layout_todo_list.visibility = View.GONE
            et_date_time.setText(exampleDateStr)
            et_title.setText("")
            et_content.setText("")
        }
        btn_add_todo.setOnClickListener {
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            val dateStr = et_date_time.text.toString()
            if (title.isEmpty()) {
                tv_tips.visibility = View.VISIBLE
                tv_tips.text = getString(R.string.str_tip_todo_title_not_empty)
            }else if (dateStr.isEmpty()) {
                tv_tips.visibility = View.VISIBLE
                tv_tips.text = getString(R.string.str_tip_date_not_empty)
            }else if (!checkDate(dateStr)) {
                tv_tips.visibility = View.VISIBLE
                tv_tips.text = getString(R.string.str_tip_date_not_valid, exampleDateStr)
            } else  {
                tv_tips.visibility = View.GONE
                layout_add_todo.visibility = View.GONE
                layout_todo_list.visibility = View.VISIBLE

                if (isUpdate) {
                    isUpdate = false
                    mViewModel.updateTodo(todo.id, title, content, dateStr, todo.status)
                }else {
                    mViewModel.addTodo(title, content, dateStr)
                }
            }
        }
        btn_cancel.setOnClickListener {
            layout_add_todo.visibility = View.GONE
            layout_todo_list.visibility = View.VISIBLE
        }
    }

    private fun checkDate(dateStr: String): Boolean {
        val nums = dateStr.split("-")
        if (nums.size == 3 && nums[0].length == 4 && nums[1].length == 2 && nums[2].length == 2) {
            return true
        }
        return false
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.status = this.status
        mViewModel.run {

            pagedList.observe(this@TodoFragment, Observer {
                adapter.submitList(it)
            })

            uiState.observe(this@TodoFragment, Observer { state ->
                if (!isRefreshFromUpdateOrDelete) {
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
                } else {
                    isRefreshFromUpdateOrDelete = false
                }
            })

            todoState.observe(this@TodoFragment, Observer { state ->
                if (state.loading) {
                    when (state.success) {
                        Constant.TODO_FINISH, Constant.TODO_REVERT -> {
                            listener?.onRefresh()
                        }
                        Constant.TODO_DELETE, Constant.TODO_UPDATE, Constant.TODO_ADD -> {
                            refreshWithNotLoading()
                        }
                    }
                } else {
                    ToastUtils.show(
                        when (state.success) {
                            Constant.TODO_FINISH -> "完成失败"
                            Constant.TODO_DELETE -> "删除失败"
                            Constant.TODO_UPDATE -> "更新失败"
                            Constant.TODO_ADD -> "添加失败"
                            else -> "操作失败"
                        }
                    )
                }
            })

            initLoad()
        }
    }

    override fun retry() {
        mViewModel.refresh()
    }

    fun refreshWithNotLoading() {
        isRefreshFromUpdateOrDelete = true
        mViewModel.refresh()
    }

    var listener: OnParentRefreshListener? = null

}

interface OnParentRefreshListener {
    fun onRefresh()
}