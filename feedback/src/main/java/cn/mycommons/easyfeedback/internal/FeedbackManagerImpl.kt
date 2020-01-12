package cn.mycommons.easyfeedback.internal

import android.content.Context
import cn.mycommons.easyfeedback.api.FbConst.LOG_POSTFIX
import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.api.FeedbackDto
import cn.mycommons.easyfeedback.api.FeedbackMetaInfo
import cn.mycommons.easyfeedback.api.IFeedbackManager
import cn.mycommons.easyfeedback.capture.ScreenCaptureManager
import cn.mycommons.easyfeedback.cdn.CdnProxy
import cn.mycommons.easyfeedback.cdn.UploadResult
import cn.mycommons.easyfeedback.internal.plugin.TimberPlugin
import cn.mycommons.easyfeedback.shake.ShakeManager
import cn.mycommons.easyfeedback.ui.FeedbackDialog
import cn.mycommons.easyfeedback.util.FeedbackHandler
import cn.mycommons.easyfeedback.util.logError
import cn.mycommons.easyfeedback.util.logInfo
import cn.mycommons.easyfeedback.util.randomLogName
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
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

    override fun init(config: FeedbackConfig) {
        this.feedbackConfig = config

        initCapture()
        initPlugin()
        CdnProxy.initCdn()
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
        val metaInfo = FeedbackMetaInfo()
        metaInfo.pkgName = context.packageName
        metaInfo.platform = "Android"
        metaInfo.time = SimpleDateFormat("yyyyMMdd HHmmss", Locale.getDefault()).format(Date())
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val labelRes = packageInfo.applicationInfo.labelRes
        metaInfo.appName = context.resources.getString(labelRes)

        feedbackDto.extraInfo = FbUtil.feedbackCallback()?.getExtraInfo()
        feedbackDto.metaInfo = metaInfo

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
        val resultKeys = mutableListOf<String>()

        files.forEach {
            val future = executorService.submit(Callable<UploadResult> {
                val file = File(it)
                var k = ""
                if (it.endsWith(LOG_POSTFIX)) {
                    k = randomLogName()
                    val newFile = File(context.externalCacheDir, k)
                    copyFile(file, newFile)
                    val result = CdnProxy.upload(newFile, k)
                    newFile.delete()
                    return@Callable result
                } else {
                    CdnProxy.upload(file, k)
                }
            })
            resultList.add(future)
        }

        resultList.forEach {
            resultKeys.add(it.get()?.key ?: "")
        }

        logInfo("resultKeys = %s", resultKeys)

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