package com.wlm.mvvm_wanandroid.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orhanobut.logger.Logger
import com.wlm.mvvm_wanandroid.common.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks,
    CoroutineScope by MainScope() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    /**
     * 布局
     */
    abstract val layoutId: Int

    /**
     * 初始化
     */
    abstract fun init()

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Logger.i("EasyPermission", "成功申请权限$perms")
    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            ToastUtils.show("已拒绝权限${sb}并不在询问")
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要${sb}权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("是")
                .setNegativeButton("否")
                .build()
                .show()
        }
    }
}