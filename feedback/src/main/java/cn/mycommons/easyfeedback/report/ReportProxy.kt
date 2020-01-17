package cn.mycommons.easyfeedback.report

import cn.mycommons.easyfeedback.report.cdn.ICdnPlatform
import cn.mycommons.easyfeedback.report.cdn.UploadResult
import cn.mycommons.easyfeedback.report.cdn.impl.DefaultCdnPlatform
import cn.mycommons.easyfeedback.internal.FbUtil
import java.io.File

/**
 * ReportProxy <br/>
 * Created by xiaqiulei on 2020-01-12.
 */
object ReportProxy {

    // private val cdnPlatform: ICdnPlatform = QiniuCdn()
    private val cdnPlatform: ICdnPlatform by lazy {
        val config = FbUtil.feedbackManager().getConfig()
        DefaultCdnPlatform(config)
    }

    fun init() {
        cdnPlatform.init()
    }

    fun upload(file: File, key: String = ""): UploadResult {
        return cdnPlatform.upload(file, key)
    }
}