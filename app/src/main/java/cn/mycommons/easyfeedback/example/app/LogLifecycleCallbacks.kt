package cn.mycommons.easyfeedback.example.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber

/**
 * LogLifecycleCallbacks <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

internal class LogLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.i("$activity....onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("$activity....onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.i("$activity....onActivityPaused")
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("$activity....onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.i("$activity....onActivityStopped")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("$activity....onActivityDestroyed")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber.i("$activity....onActivitySaveInstanceState")
    }
}