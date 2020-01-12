package cn.mycommons.easyfeedback.feedback_timber

import android.content.Context
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque

/**
 * LoggerDispatch <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

class LogDispatch {

    internal var context: Context? = null

    private val logFile: File by lazy {
        val parent = File(context!!.externalCacheDir, "log")
        if (!parent.exists()) {
            parent.mkdir()
        }
        val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        File(parent, "${format.format(Date())}.timber.log.txt")
    }

    private val logWriter: PrintWriter by lazy {
        val stream = FileOutputStream(logFile, true)
        PrintWriter(BufferedWriter(OutputStreamWriter(stream)))
    }

    private var executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var queue: Queue<LogMsg> = LinkedBlockingDeque()
    private var format = SimpleDateFormat("HH:mm:ss.sss", Locale.getDefault())
    private val logLevel: Map<Int, String> = mapOf(
            Log.VERBOSE to "V",
            Log.DEBUG to "D",
            Log.INFO to "I",
            Log.WARN to "W",
            Log.ERROR to "E"
    )

    init {
        executorService.execute {
            while (true) {
                queue.poll()?.apply {
                    val msg = "${format.format(Date())} ${getLevel(priority)} $tag $message"
                    logWriter.println(msg)
                    logWriter.flush()
                }
            }
        }
    }

    fun dispatch(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= Log.INFO) {
            val msg = LogMsg(priority, tag, message, t)
            queue.offer(msg)
        }
    }

    private fun getLevel(priority: Int): String {
        return logLevel[priority] ?: "Unknown"
    }

    internal fun getCurrentLogFile() = logFile

    internal fun getLogDir(): File? = logFile.parentFile
}