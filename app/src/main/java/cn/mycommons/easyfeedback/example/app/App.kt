package cn.mycommons.easyfeedback.example.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import cn.mycommons.easyfeedback.FeedbackHelper
import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.api.IFeedbackCallback
import cn.mycommons.easyfeedback.example.BuildConfig

/**
 * App <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class App : MultiDexApplication() {

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
            qiniuCdn = true

            FeedbackHelper.init(this)
            FeedbackHelper.feedbackCallback = object : IFeedbackCallback {

                override fun getExtraInfo(): Map<String, Any?>? {
                    val map = linkedMapOf<String, Any?>()
                    map["package"] = packageName
                    map["platform"] = "Android"
                    map["versionName"] = BuildConfig.VERSION_NAME
                    map["versionCode"] = BuildConfig.VERSION_CODE

                    map["user_id"] = System.currentTimeMillis()
                    return map
                }
            }
        }
    }
}