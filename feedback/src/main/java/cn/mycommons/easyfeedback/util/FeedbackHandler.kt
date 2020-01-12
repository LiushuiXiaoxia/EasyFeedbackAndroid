package cn.mycommons.easyfeedback.util

import android.os.Handler
import android.os.Looper

/**
 * FeedbackHandlerUtil <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

object FeedbackHandler {

    private val handler = Handler(Looper.getMainLooper())


    fun post(runnable: () -> Unit) {
        handler.post(runnable)
    }
}