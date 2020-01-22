package cn.mycommons.easyfeedback.internal.report

import cn.mycommons.easyfeedback.internal.FbUtil
import cn.mycommons.easyfeedback.internal.report.cdn.ICdnPlatform
import cn.mycommons.easyfeedback.internal.report.cdn.UploadResult
import cn.mycommons.easyfeedback.internal.report.cdn.impl.DefaultCdnPlatform
import cn.mycommons.easyfeedback.internal.report.cdn.qiniu.QiniuCdnPlatform
import java.io.File

/**
 * ReportProxy <br/>
 * Created by xiaqiulei on 2020-01-12.
 */
object ReportProxy {

    private val cdnPlatform: ICdnPlatform by lazy {
        val config = FbUtil.feedbackManager().getConfig()
        var platform: ICdnPlatform = DefaultCdnPlatform(config)

        if (config.qiniuCdn) {
            val qiniuCdnPlatform = QiniuCdnPlatform(config)
            if (qiniuCdnPlatform.tryInit()) {
                platform = qiniuCdnPlatform
            }
        }
        return@lazy platform
    }

    fun onCreate() {
        cdnPlatform.onCreate()
    }

    fun upload(file: File, key: String = ""): UploadResult {
        return cdnPlatform.upload(file, key)
    }
}