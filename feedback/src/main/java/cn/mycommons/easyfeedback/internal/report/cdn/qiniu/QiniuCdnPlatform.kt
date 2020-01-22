package cn.mycommons.easyfeedback.internal.report.cdn.qiniu

import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.internal.FbUtil
import cn.mycommons.easyfeedback.internal.api.HttpApi
import cn.mycommons.easyfeedback.internal.report.cdn.CdnConst.DEFAULT_CDN
import cn.mycommons.easyfeedback.internal.report.cdn.ICdnPlatform
import cn.mycommons.easyfeedback.internal.report.cdn.UploadResult
import com.qiniu.android.collect.Config
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import java.io.File


/**
 * QiniuCdn <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class QiniuCdnPlatform(config: FeedbackConfig) : ICdnPlatform {

    private var uploadManager: UploadManager? = null

    val api: HttpApi by lazy { HttpApi(config) }

    override fun tryInit(): Boolean {
        if (hasQiniuSdk()) {
            val config: Configuration = Configuration.Builder()
                    .connectTimeout(10)
                    .useHttps(true)
                    .responseTimeout(60)
                    .zone(FixedZone.zone0)
                    .build()

            Config.dnscacheDir = File(FbUtil.context().cacheDir, "dnsCache").absolutePath
            uploadManager = UploadManager(config, 3)
            return true
        }
        return false
    }

    override fun onCreate() {
    }

    override fun upload(file: File, key: String): UploadResult {
        val token = token()

        var k = key
        if (k.isBlank()) {
            k = file.name
        }

        val info = uploadManager?.syncPut(file, k, token, null)

        val result = UploadResult()
        result.platform = DEFAULT_CDN
        result.success = info?.isOK ?: false
        if (result.success) {
            result.url = info?.response?.getString("key") ?: k
        }
        return result
    }

    private fun token(): String {
        return api.getQiniuToken(FbUtil.context().packageName, "Android")!!
    }
}