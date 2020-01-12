package cn.mycommons.easyfeedback.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import cn.mycommons.easyfeedback.capture.ScreenCaptureManager
import cn.mycommons.easyfeedback.shake.ShakeManager

/**
 * FeedbackAppli <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

internal class FbActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    var currentActivity: Activity? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity

        if (FbUtil.feedbackManager().getConfig().autoClose) {
            ShakeManager.start()
        }
        ScreenCaptureManager.start()
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = null
        if (FbUtil.feedbackManager().getConfig().autoClose) {
            ShakeManager.stop()
        }

        ScreenCaptureManager.stop()
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }
}