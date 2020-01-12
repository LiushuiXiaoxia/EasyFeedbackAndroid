package cn.mycommons.easyfeedback.internal

import android.app.Activity
import android.content.Context
import cn.mycommons.easyfeedback.FeedbackHelper
import cn.mycommons.easyfeedback.api.IFeedbackCallback
import cn.mycommons.easyfeedback.api.IFeedbackManager

/**
 * FbUtil <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

internal object FbUtil {

    fun context(): Context = FeedbackHelper.context

    fun getActivity(): Activity? = FeedbackHelper.activityLifecycleCallbacks.currentActivity

    fun feedbackManager(): IFeedbackManager = FeedbackHelper.feedbackManager

    fun feedbackCallback(): IFeedbackCallback? = FeedbackHelper.feedbackCallback
}