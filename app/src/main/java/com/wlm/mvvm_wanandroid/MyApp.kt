package com.wlm.mvvm_wanandroid

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class MyApp : MultiDexApplication() {

    companion object {
        const val TAG = "logger_tag"
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLogger()
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    private fun initLogger() {
        val strategy = PrettyFormatStrategy.newBuilder()
            .tag(TAG)
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(strategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Logger.d("${activity.componentName.className} onCreated")
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {
            Logger.d("${activity.componentName.className} onResumed")
        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Logger.d("${activity.componentName.className} onDestroyed")
        }

    }
}