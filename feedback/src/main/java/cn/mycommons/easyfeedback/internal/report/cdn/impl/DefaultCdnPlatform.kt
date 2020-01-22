package cn.mycommons.easyfeedback.internal.report.cdn.impl

import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.internal.api.HttpApi
import cn.mycommons.easyfeedback.internal.report.cdn.CdnConst
import cn.mycommons.easyfeedback.internal.report.cdn.ICdnPlatform
import cn.mycommons.easyfeedback.internal.report.cdn.UploadResult
import cn.mycommons.easyfeedback.util.logInfo
import java.io.File

internal class DefaultCdnPlatform(private val config: FeedbackConfig) : ICdnPlatform {

    val api: HttpApi by lazy { HttpApi(config) }

    override fun tryInit(): Boolean {
        return true
    }

    override fun onCreate() {
    }

    override fun upload(file: File, key: String): UploadResult {
        val resp = api.upload(file, key)

        val result = UploadResult()
        result.platform = CdnConst.DEFAULT_CDN
        result.success = resp != null && resp.isSuccess()
        result.url = resp?.data?.downloadUrl

        logInfo("upload.result = $result")
        return result
    }
}