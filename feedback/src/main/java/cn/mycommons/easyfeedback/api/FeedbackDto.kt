package cn.mycommons.easyfeedback.api

/**
 * FeedbackDto <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class FeedbackDto {

    var contact: String? = null

    var desc: String? = null

    var images: List<String>? = null

    var metaInfo: FeedbackMetaInfo? = null

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