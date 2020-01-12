package cn.mycommons.easyfeedback.feedback_timber

/**
 * LogMsg <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

data class LogMsg(
    var priority: Int = 0,
    var tag: String? = null,
    var message: String = "",
    var t: Throwable? = null
)