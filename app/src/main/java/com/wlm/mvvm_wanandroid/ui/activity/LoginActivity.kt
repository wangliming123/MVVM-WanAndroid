package com.wlm.mvvm_wanandroid.ui.activity

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.base.ui.BaseVMActivity
import com.wlm.mvvm_wanandroid.common.Constant
import com.wlm.mvvm_wanandroid.common.net.RetrofitManager
import com.wlm.mvvm_wanandroid.common.utils.SharedPrefs
import com.wlm.mvvm_wanandroid.common.utils.ToastUtils
import com.wlm.mvvm_wanandroid.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class LoginActivity : BaseVMActivity<LoginViewModel>() {
    override val providerVMClass: Class<LoginViewModel> = LoginViewModel::class.java
    override val layoutId: Int = R.layout.activity_login

    private var isLogin by SharedPrefs(Constant.IS_LOGIN, false)
    private var userString by SharedPrefs(Constant.USER_STRING, "")

    override fun init() {
        super.init()
        initView()
        setSupportActionBar(toolbar)
        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initView() {
        val username = userString.split(",")[0]
        et_username.setText(username)
        btn_login.setOnClickListener { login() }
        btn_register.setOnClickListener { register() }
    }

    private fun login() {
        if (check()) {
            mViewModel.login(et_username.text.toString(), et_password.text.toString())
        }
    }

    private fun register() {
        if (check()) {
            mViewModel.register(et_username.text.toString(), et_password.text.toString())
        }
    }

    private fun check(): Boolean {
        if (et_username.text.toString().length < 3) {
            tv_tips.visibility = View.VISIBLE
            tv_tips.text = getString(R.string.str_username_invalid)
            return false
        }
        if (et_password.text.toString().length < 6) {
            tv_tips.visibility = View.VISIBLE
            tv_tips.text = getString(R.string.str_password_invalid)
            return false
        }
        tv_tips.visibility = View.GONE
        return true
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.run {
            uiState.observe(this@LoginActivity, Observer { state ->
                showLoading(state.loading)

                state.error?.let {
                    ToastUtils.show(it)
                }

                state.success?.let {
                    isLogin = true
                    userString = it
                    finish()
                }
            })
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) loginProgress.visibility = View.VISIBLE else loginProgress.visibility = View.GONE
    }

    override fun onError(e: Throwable) {
        Logger.d("login or register error", e.message)
    }
}
