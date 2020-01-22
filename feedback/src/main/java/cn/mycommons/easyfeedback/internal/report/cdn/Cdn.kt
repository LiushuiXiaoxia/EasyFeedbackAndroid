package cn.mycommons.easyfeedback.internal.report.cdn

import java.io.File

/**
 * DefaultCdn <br/>
 * Created by xiaqiulei on 2020-01-17.
 */

object CdnConst {

    const val DEFAULT_CDN = "default"

    const val QINIU_CDN = "qiniu"
}

interface ICdnPlatform {

    fun tryInit(): Boolean

    fun onCreate()

    fun upload(file: File, key: String): UploadResult
}

data class UploadResult(
        var platform: String? = null,
        var url: String? = null,
        var success: Boolean = false
)


