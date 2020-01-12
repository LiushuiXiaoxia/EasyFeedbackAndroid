package cn.mycommons.easyfeedback.api

/**
 * IFeedbackCallback <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

interface IFeedbackCallback {

    /**
     * 获取必要的信息
     */
    fun getExtraInfo(): Map<String, Any?>?

    /**
     * 需要一起上传的文件，如日志文件等
     */
    fun getExtraFile(): List<String>
}