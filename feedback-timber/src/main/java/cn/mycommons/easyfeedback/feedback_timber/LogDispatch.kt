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

    companion object {
        const val BATCH_SIZE = 10

        const val TIME_INTERVAL = 5_000L
    }

    internal var context: Context? = null

    private val logFile: File by lazy {
        val parent = File(context!!.externalCacheDir, "log")
        if (!parent.exists()) {
            parent.mkdir()
        }
        val format = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        File(parent, "${format.format(Date())}_timber.log.txt")
    }

    private val logWriter: PrintWriter by lazy {
        val stream = FileOutputStream(logFile, true)
        PrintWriter(BufferedWriter(OutputStreamWriter(stream)))
    }

    private var executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var queue: LinkedBlockingDeque<LogMsg> = LinkedBlockingDeque()
    private var format = SimpleDateFormat("HH:mm:ss.sss", Locale.getDefault())
    private val logLevel: Map<Int, String> = mapOf(
            Log.VERBOSE to "VERBOSE",
            Log.DEBUG to "DEBUG",
            Log.INFO to "INFO",
            Log.WARN to "WARN",
            Log.ERROR to "ERROR"
    )

    init {
        executorService.execute {
            while (true) {
                val first = queue.takeFirst()

                val list = LinkedList<LogMsg>()
                queue.drainTo(list, BATCH_SIZE) // 一次性取多个
                list.addFirst(first)

                // Log.i("LogDispatch", "edf --> ${list.size}")
                list.forEach {
                    val msg = "${format.format(Date())} ${getLevel(it.priority)} ${it.tag} ${it.message}"
                    logWriter.println(msg)
                    logWriter.flush()
                }
                if (list.size <= 1) {
                    Thread.sleep(TIME_INTERVAL)
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