package cn.mycommons.easyfeedback.internal

import cn.mycommons.easyfeedback.shake.OnShakeCallback
import cn.mycommons.easyfeedback.ui.FeedbackDialog


/**
 * UiHellper <br/>
 * Created by xiaqiulei on 2020-01-11.
 */

class FeedbackShakeCallback : OnShakeCallback {

    override fun onShake() {
        FbUtil.getActivity()?.let {
            FeedbackDialog(it).showDialog()
        }
    }
}