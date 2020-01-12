package cn.mycommons.easyfeedback.cdn.qiniu

import cn.mycommons.easyfeedback.cdn.ICdnPlatform
import cn.mycommons.easyfeedback.cdn.UploadResult
import cn.mycommons.easyfeedback.internal.FbUtil
import cn.mycommons.easyfeedback.util.MetaDataUtil
import com.qiniu.android.collect.Config
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import com.qiniu.util.Auth
import java.io.File


/**
 * QiniuCdn <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class QiniuCdn : ICdnPlatform {

    private var uploadManager: UploadManager? = null

    override fun init() {
        val config: Configuration = Configuration.Builder()
                .connectTimeout(10)
                .useHttps(true)
                .responseTimeout(60)
                .zone(FixedZone.zone0)
                .build()

        Config.dnscacheDir = File(FbUtil.context().cacheDir, "dnsCache").absolutePath
        uploadManager = UploadManager(config, 3)
    }

    override fun upload(file: File, key: String): UploadResult {
        val token = token()

        var k = key
        if (k.isBlank()) {
            k = file.name
        }

        val info = uploadManager?.syncPut(file, k, token, null)

        val result = UploadResult()
        result.success = info?.isOK ?: false
        if (result.success) {
            result.key = info?.response?.getString("key") ?: k
        }
        return result
    }

    private fun token(): String {
        val accessKey = MetaDataUtil.get("QINIU_AK")
        val secretKey = MetaDataUtil.get("QINIU_SK")
        val bucket = "mobile-feedback"
        val auth = Auth.create(accessKey, secretKey)
        return auth.uploadToken(bucket)
    }
}