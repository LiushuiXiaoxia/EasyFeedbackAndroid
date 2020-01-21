package cn.mycommons.easyfeedback.internal

import android.content.Context
import cn.mycommons.easyfeedback.api.*
import cn.mycommons.easyfeedback.api.FbConst.LOG_POSTFIX
import cn.mycommons.easyfeedback.capture.ScreenCaptureManager
import cn.mycommons.easyfeedback.internal.api.HttpApi
import cn.mycommons.easyfeedback.internal.plugin.TimberPlugin
import cn.mycommons.easyfeedback.report.ReportProxy
import cn.mycommons.easyfeedback.report.cdn.UploadResult
import cn.mycommons.easyfeedback.shake.ShakeManager
import cn.mycommons.easyfeedback.ui.FeedbackDialog
import cn.mycommons.easyfeedback.util.FeedbackHandler
import cn.mycommons.easyfeedback.util.logError
import cn.mycommons.easyfeedback.util.logInfo
import cn.mycommons.easyfeedback.util.randomLogName
import java.io.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * FeedbackManagerImpl <br/>
 * Created by xiaqiulei on 2020-01-11.
 */
class FeedbackManagerImpl(var context: Context) : IFeedbackManager {

    private val executorService = Executors.newFixedThreadPool(10)
    private var feedbackConfig: FeedbackConfig? = null
    private val httpApi: HttpApi by lazy { HttpApi(FbUtil.feedbackManager().getConfig()) }

    override fun init(config: FeedbackConfig) {
        this.feedbackConfig = config

        initCapture()
        initPlugin()
        ReportProxy.onCreate()
    }

    private fun initCapture() {
        ScreenCaptureManager.onCaptureChangeCallback = object : ScreenCaptureManager.OnCaptureChangeCallback {
            override fun onCapture(path: String?) {
                FeedbackHandler.post {
                    FbUtil.getActivity()?.let {
                        FeedbackDialog(it, File(path!!)).showDialog()
                    }
                }
            }
        }
    }

    private fun initPlugin() {
        TimberPlugin().tryInit()
    }

    override fun getConfig(): FeedbackConfig = feedbackConfig!!

    override fun submit(feedbackDto: FeedbackDto,
                        onSubmitCallback: IFeedbackManager.OnSubmitCallback?) {
        executorService.submit {
            doSubmit(feedbackDto)
        }
    }

    private fun doSubmit(feedbackDto: FeedbackDto) {
        logInfo("feedbackDto = %s", feedbackDto)

        feedbackDto.metaInfo = FeedbackMetaInfo().apply { init(context) }
        feedbackDto.extraInfo = FbUtil.feedbackCallback()?.getExtraInfo()

        logInfo("feedbackDto = %s", feedbackDto)

        val files = mutableListOf<String>()
        files.addAll(feedbackDto.images ?: listOf())

        FbUtil.feedbackCallback()?.getExtraFile()?.forEach {
            val file = File(it)
            if (file.exists() && file.isFile) {
                files.add(it)
            }
        }

        val resultList = mutableListOf<Future<UploadResult>>()
        for (it in files) {
            val future = executorService.submit(Callable<UploadResult> {
                val file = File(it)
                var k = ""
                if (it.endsWith(LOG_POSTFIX)) {
                    k = randomLogName()
                    val newFile = File(context.externalCacheDir, k)
                    copyFile(file, newFile)
                    val result = ReportProxy.upload(newFile, k)
                    newFile.delete()
                    return@Callable result
                } else {
                    return@Callable ReportProxy.upload(file, k)
                }
            })
            resultList.add(future)
        }

        val resultKeys = mutableListOf<String>()
        resultList.forEach {
            resultKeys.add(it.get()?.url ?: "")
        }

        logInfo("resultKeys = %s", resultKeys)
        feedbackDto.images = resultKeys

        httpApi.feedback(feedbackDto)

        ShakeManager.restart()
    }

    private fun copyFile(source: File, dest: File) {
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = FileInputStream(source)
            output = FileOutputStream(dest)
            val buf = ByteArray(1024)
            var len = input.read(buf)
            while (len != -1) {
                output.write(buf, 0, len)
                len = input.read(buf)
            }

            output.flush()
        } catch (t: Throwable) {
            logError("copyFile fail", t)
        } finally {
            input?.close()
            output?.close()
        }
    }
}