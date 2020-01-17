package cn.mycommons.easyfeedback.example.app

import android.app.Application
import android.content.Context
import cn.mycommons.easyfeedback.FeedbackHelper
import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.api.IFeedbackCallback
import cn.mycommons.easyfeedback.example.BuildConfig
import cn.mycommons.easyfeedback.feedback_timber.FbTimberHelper
import java.util.*
import kotlin.collections.HashMap

/**
 * App <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        registerActivityLifecycleCallbacks(LogLifecycleCallbacks())
    }

    override fun onCreate() {
        super.onCreate()

        FeedbackConfig().apply {
            debug = BuildConfig.DEBUG
            shakeFeedback = false
            uploadServer = BuildConfig.UPLOAD_SERVER

            FeedbackHelper.init(this)
            FeedbackHelper.feedbackCallback = object : IFeedbackCallback {

                override fun getExtraInfo(): Map<String, Any?>? {
                    val map = HashMap<String, Any?>()
                    map["package"] = packageName
                    map["platform"] = "Android"
                    map["versionName"] = BuildConfig.VERSION_NAME
                    map["versionCode"] = BuildConfig.VERSION_CODE

                    map["user_id"] = System.currentTimeMillis()
                    return map
                }

                override fun getExtraFile(): List<String> {
                    // return listOf(FbTimberHelper.getCurrentLogFile().absolutePath)
                    return Collections.emptyList()
                }
            }
        }
    }
}