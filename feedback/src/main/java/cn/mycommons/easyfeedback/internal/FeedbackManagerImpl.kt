package cn.mycommons.easyfeedback.internal

import android.content.Context
import cn.mycommons.easyfeedback.api.*
import cn.mycommons.easyfeedback.capture.ScreenCaptureManager
import cn.mycommons.easyfeedback.internal.api.HttpApi
import cn.mycommons.easyfeedback.internal.plugin.impl.TimberPlugin
import cn.mycommons.easyfeedback.internal.report.ReportProxy
import cn.mycommons.easyfeedback.internal.report.cdn.UploadResult
import cn.mycommons.easyfeedback.shake.ShakeManager
import cn.mycommons.easyfeedback.ui.FeedbackDialog
import cn.mycommons.easyfeedback.util.FeedbackHandler
import cn.mycommons.easyfeedback.util.logInfo
import java.io.File
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
        ReportProxy.onCreate()
        initPlugin()
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
        TimberPlugin.tryInit()
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
        FbUtil.feedbackCallback()?.getExtraFile()?.forEach {
            val file = File(it)
            if (file.exists() && file.isFile) {
                files.add(it)
            }
        }
        TimberPlugin.archivingFile()?.let {
            if (it.exists()) {
                files.add(it.absolutePath)
            }
        }

        feedbackDto.images = batchUpload(feedbackDto.images ?: listOf())
        feedbackDto.files = batchUpload(files)

        httpApi.feedback(feedbackDto)

        ShakeManager.restart()
    }

    private fun batchUpload(files: List<String>): List<String> {
        val resultList = mutableListOf<Future<UploadResult>>()
        for (it in files) {
            val future = executorService.submit(Callable<UploadResult> {
                val file = File(it)
                return@Callable ReportProxy.upload(file, file.name)
            })
            resultList.add(future)
        }

        val resultUrl = mutableListOf<String>()
        resultList.forEach {
            resultUrl.add(it.get()?.url ?: "")
        }
        logInfo("resultKeys = %s", resultUrl)

        return resultUrl
    }
}