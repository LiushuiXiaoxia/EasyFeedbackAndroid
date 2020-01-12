package cn.mycommons.easyfeedback.feedback_timber

import android.content.Context
import timber.log.Timber

/**
 * FbTimberHelper <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

object FbTimberHelper {

    private val logDispatch: LogDispatch = LogDispatch()

    fun init(context: Context) {
        logDispatch.context = context
        Timber.plant(object : Timber.DebugTree() {

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, tag, message, t)

                logDispatch.dispatch(priority, tag, message, t)
            }
        })
    }

    fun getCurrentLogFile() = logDispatch.getCurrentLogFile()

    fun getLogDir() = logDispatch.getLogDir()
}