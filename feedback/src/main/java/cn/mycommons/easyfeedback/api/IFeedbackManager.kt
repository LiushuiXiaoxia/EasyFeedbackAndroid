package cn.mycommons.easyfeedback.api

/**
 * IFeedback <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

interface IFeedbackManager {

    fun init(config: FeedbackConfig)

    fun getConfig(): FeedbackConfig

    fun submit(feedbackDto: FeedbackDto, onSubmitCallback: OnSubmitCallback?)

    interface OnSubmitCallback {

        fun onSuccess()

        fun onFail()
    }
}