package cn.mycommons.easyfeedback.report.cdn.qiniu

import cn.mycommons.easyfeedback.util.logE
import com.qiniu.android.storage.Configuration

/**
 * QiniuUtil <br/>
 * Created by xiaqiulei on 2020-01-21.
 */

fun hasQiniuSdk(): Boolean {

    return try {
        val name = Configuration::class.java
        logE(msg = "hasQiniuSdk $name true")
        true
    } catch (e: Throwable) {
        logE(msg = "hasQiniuSdk false")
        false
    }
}