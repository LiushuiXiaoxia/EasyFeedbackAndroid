package cn.mycommons.easyfeedback.util

import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.os.Bundle
import cn.mycommons.easyfeedback.internal.FbUtil


/**
 * MetaUtil <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

object MetaDataUtil {

    fun get(key: String?): String {
        try {
            val manager = FbUtil.context().packageManager
            val packageName = FbUtil.context().packageName
            val bundle: Bundle = manager.getApplicationInfo(packageName, GET_META_DATA).metaData
            return "${bundle[key]}"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}