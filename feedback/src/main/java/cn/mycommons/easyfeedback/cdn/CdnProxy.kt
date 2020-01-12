package cn.mycommons.easyfeedback.cdn

import cn.mycommons.easyfeedback.cdn.qiniu.QiniuCdn
import java.io.File

/**
 * CdnProxy <br/>
 * Created by xiaqiulei on 2020-01-12.
 */


interface ICdnPlatform {

    fun init()

    fun upload(file: File, key: String): UploadResult
}


data class UploadResult(
        var key: String? = null,
        var success: Boolean = false
)

object CdnProxy {

    private val cdnPlatform: ICdnPlatform = QiniuCdn()

    fun initCdn() {
        cdnPlatform.init()
    }

    fun upload(file: File, key: String = ""): UploadResult {
        return cdnPlatform.upload(file, key)
    }
}