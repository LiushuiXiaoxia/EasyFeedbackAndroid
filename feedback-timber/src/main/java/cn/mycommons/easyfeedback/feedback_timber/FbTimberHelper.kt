package cn.mycommons.easyfeedback.feedback_timber

import android.content.Context
import timber.log.Timber
import java.io.File

/**
 * FbTimberHelper <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

object FbTimberHelper {

    private lateinit var logDispatch: LogDispatch

    fun init(context: Context) {
        logDispatch = LogDispatch(context)
        Timber.plant(object : Timber.DebugTree() {

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, tag, message, t)

                logDispatch.dispatch(priority, tag, message, t)
            }
        })
    }

    /**
     * 归档文件
     */
    fun archivingFile(): File = logDispatch.archivingFile()
}