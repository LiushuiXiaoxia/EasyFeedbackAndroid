package cn.mycommons.easyfeedback.feedback_timber

import android.content.Context
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * LoggerDispatch <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

class LogDispatch(val context: Context) {

    companion object {
        const val BATCH_SIZE = 10

        const val TIME_INTERVAL = 5_000L
    }

    private var queue: LinkedBlockingDeque<LogMsg> = LinkedBlockingDeque()
    private var executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var format = SimpleDateFormat("HH:mm:ss.sss", Locale.getDefault())
    private val logLevel: Map<Int, String> = mapOf(
            Log.VERBOSE to "VERBOSE",
            Log.DEBUG to "DEBUG",
            Log.INFO to "INFO",
            Log.WARN to "WARN",
            Log.ERROR to "ERROR"
    )

    private lateinit var logFile: File
    private lateinit var logWriter: PrintWriter

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

        initFile(true)
    }

    private fun initFile(removeZip: Boolean) {
        val parent = logDir()
        val format = SimpleDateFormat("yyyyMMdd.HHmmss.SSS", Locale.getDefault())
        logFile = File(parent, "${context.packageName}_${format.format(Date())}_timber.log")
        logWriter = PrintWriter(BufferedWriter(OutputStreamWriter(FileOutputStream(logFile, true))))

        if (removeZip) {
            zipDir().listFiles()?.forEach {
                it.deleteOnExit()
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

    internal fun archivingFile(): File {
        val oldFile = logFile
        val oldLogWriter = logWriter

        initFile(false) // 重新生成

        oldLogWriter.flush()
        oldLogWriter.close()

        val gzParent = zipDir()
        val gzFile = File(gzParent, oldFile.name.replace("_timber.log", ".gz"))
        val zos = ZipOutputStream(FileOutputStream(gzFile))

        logDir().listFiles()?.forEach {
            if (!it.isDirectory) {
                val entry = ZipEntry(it.name)
                zos.putNextEntry(entry)
                copyIntoZip(zos, FileInputStream(it))
                // 不是当前文件则删除
                if (it.name != logFile.name) {
                    it.deleteOnExit()
                }
            }
        }
        zos.close()

        return gzFile
    }

    private fun zipDir(): File {
        return File(context.externalCacheDir, "zip").apply { mkdir() }
    }

    private fun logDir(): File {
        return File(context.externalCacheDir, "log").apply { mkdir() }
    }

    private fun copyIntoZip(output: OutputStream, input: InputStream) {
        try {
            val buf = ByteArray(1024)
            var len = input.read(buf)
            while (len != -1) {
                output.write(buf, 0, len)
                len = input.read(buf)
            }
            output.flush()
        } catch (t: Throwable) {
        } finally {
            input.close()
        }
    }
}