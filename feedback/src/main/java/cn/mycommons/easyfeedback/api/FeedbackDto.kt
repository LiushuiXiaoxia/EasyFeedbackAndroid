package cn.mycommons.easyfeedback.api

import android.content.Context
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * FeedbackDto <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class FeedbackDto {

    var desc: String? = null
    var contact: String? = null

    @SerializedName("images")
    var images: List<String>? = null

    @SerializedName("meta")
    var metaInfo: FeedbackMetaInfo? = null

    @SerializedName("extra")
    var extraInfo: Map<String, Any?>? = null

    override fun toString(): String {
        return "FeedbackDto(contact=$contact, desc=$desc, images=$images, metaInfo=$metaInfo, extraInfo=$extraInfo)"
    }
}

class FeedbackMetaInfo {

    var time: String? = null

    var pkgName: String? = null

    var platform: String = "Android"

    var appName: String? = null

    override fun toString(): String {
        return "FeedbackMetaInfo(time=$time, pkgName=$pkgName, platform='$platform', appName=$appName)"
    }
}

fun FeedbackMetaInfo.init(context: Context) {
    pkgName = context.packageName
    platform = "Android"
    time = SimpleDateFormat("yyyyMMdd HHmmss", Locale.getDefault()).format(Date())
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val labelRes = packageInfo.applicationInfo.labelRes
    appName = context.resources.getString(labelRes)
}