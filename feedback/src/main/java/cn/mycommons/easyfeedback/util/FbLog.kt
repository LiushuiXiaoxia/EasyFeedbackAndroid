package cn.mycommons.easyfeedback.util

import android.util.Log
import cn.mycommons.easyfeedback.FeedbackHelper

/**
 * FbLog <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

private const val FB_LOG_TAG = "EasyFeedback"

private fun isLogEnable(): Boolean = FeedbackHelper.feedbackManager.getConfig().debug


fun logD(tag: String = FB_LOG_TAG, msg: String, vararg args: Any?) {
    if (isLogEnable()) {
        var m = msg
        if (args.isNotEmpty()) {
            m = msg.format(*args)
        }
        Log.d(tag, m)
    }
}

fun logI(tag: String = FB_LOG_TAG, msg: String, vararg args: Any?) {
    if (isLogEnable()) {
        var m = msg
        if (args.isNotEmpty()) {
            m = msg.format(*args)
        }
        Log.i(tag, m)
    }
}

fun logE(tag: String = FB_LOG_TAG, msg: String, vararg args: Any?) {
    if (isLogEnable()) {
        var m = msg
        if (args.isNotEmpty()) {
            m = msg.format(*args)
        }
        Log.e(tag, m)
    }
}

fun logE(tag: String = FB_LOG_TAG, msg: String, throwable: Throwable) {
    if (isLogEnable()) {
        Log.e(tag, msg, throwable)
    }
}

fun Any.logDebug(msg: String, vararg args: Any?) {
    logD(this.javaClass.simpleName, msg, *args)
}

fun Any.logInfo(msg: String, vararg args: Any?) {
    logI(this.javaClass.simpleName, msg, *args)
}

fun Any.logError(msg: String, vararg args: Any?) {
    logE(this.javaClass.simpleName, msg, * args)
}

fun Any.logError(msg: String, throwable: Throwable) {
    logE(this.javaClass.simpleName, msg, throwable)
}