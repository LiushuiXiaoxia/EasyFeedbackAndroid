package cn.mycommons.easyfeedback

import android.app.Activity
import android.app.Application
import android.content.Context
import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.api.IFeedbackCallback
import cn.mycommons.easyfeedback.api.IFeedbackManager
import cn.mycommons.easyfeedback.internal.FbActivityLifecycleCallbacks
import cn.mycommons.easyfeedback.internal.FeedbackManagerImpl
import cn.mycommons.easyfeedback.internal.FeedbackShakeCallback
import cn.mycommons.easyfeedback.shake.ShakeManager
import cn.mycommons.easyfeedback.ui.FeedbackDialog
import cn.mycommons.easyfeedback.util.logInfo

/**
 * FeedbackHelper <br></br>
 * Created by xiaqiulei on 2020-01-11.
 */
object FeedbackHelper {

    internal lateinit var context: Context
    internal lateinit var feedbackManager: IFeedbackManager
    internal lateinit var activityLifecycleCallbacks: FbActivityLifecycleCallbacks
    var feedbackCallback: IFeedbackCallback? = null

    /**
     * 初始化
     */
    fun init(config: FeedbackConfig) {
        feedbackManager = FeedbackManagerImpl(context)
        feedbackManager.init(config)

        activityLifecycleCallbacks = FbActivityLifecycleCallbacks()

        logInfo("FeedbackHelper.init")

        start()
    }

    /**
     * 开启功能
     */
    fun start() {
        val application = context.applicationContext as Application
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        ShakeManager.onShakeCallback =
            FeedbackShakeCallback()
    }


    /**
     * 关闭功能
     */
    fun stop() {
        val application = context.applicationContext as Application
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        ShakeManager.onShakeCallback = null
    }

    /**
     * 主动显示对话
     */
    fun showDialog(activity: Activity) {
        FeedbackDialog(activity).showDialog()
    }
}