package cn.mycommons.easyfeedback.api

/**
 * FeedbackConfig <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class FeedbackConfig {

    /**
     * 是否debug模式，如果为true则开启日志等功能，默认为false
     */
    var debug: Boolean = false

    /**
     * 是否自动关闭，在Activity的onStart、onStop方法自动开启和关闭，默认为true
     */
    var autoClose: Boolean = true
}