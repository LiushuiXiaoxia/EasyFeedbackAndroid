package cn.mycommons.easyfeedback.internal.plugin

import cn.mycommons.easyfeedback.util.logDebug
import cn.mycommons.easyfeedback.util.logError

/**
 * BasePlugin <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

abstract class BasePlugin(val name: String) {

    fun tryInit() {
        try {
            plugin().javaClass
            logDebug("FbPlugin $name begin init")
            onInit()
            logDebug("FbPlugin $name end init")
        } catch (t: Throwable) {
            logError("FbPlugin do not has plugin $name")
        }
    }

    abstract fun plugin(): Class<*>

    abstract fun onInit()
}