package cn.mycommons.easyfeedback.internal.plugin

import cn.mycommons.easyfeedback.feedback_timber.FbTimberHelper
import cn.mycommons.easyfeedback.internal.FbUtil

/**
 * TimberPlugin <br/>
 * Created by xiaqiulei on 2020-01-12.
 */
class TimberPlugin : BasePlugin("TimberPlugin") {

    override fun plugin(): Class<*> {
        return FbTimberHelper.javaClass
    }

    override fun onInit() {
        FbTimberHelper.init(FbUtil.context())
    }
}