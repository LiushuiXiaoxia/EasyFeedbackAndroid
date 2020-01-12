package cn.mycommons.easyfeedback.util

import cn.mycommons.easyfeedback.internal.FbUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * FileNameUtil <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

fun randomLogName(): String {
    val s = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss.sss", Locale.getDefault()).format(Date())
    return "feedback_${s}_${FbUtil.context().packageName}.timber.log.txt"
}

fun randomImageName(): String {
    val s = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss.sss", Locale.getDefault()).format(Date())
    return "feedback_${s}_${FbUtil.context().packageName}.png"
}