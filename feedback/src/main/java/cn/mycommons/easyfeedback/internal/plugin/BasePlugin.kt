package cn.mycommons.easyfeedback.internal.plugin

import cn.mycommons.easyfeedback.util.logDebug
import cn.mycommons.easyfeedback.util.logError

/**
 * BasePlugin <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

abstract class BasePlugin(val name: String) {

    var enable = false
        private set

    fun tryInit() {
        try {
            pluginClass().name
            logDebug("FbPlugin $name begin init")
            doInit()
            enable = true
            logDebug("FbPlugin $name end init")
        } catch (t: Throwable) {
            logError("FbPlugin do not has plugin $name")
        }
    }

    internal abstract fun pluginClass(): Class<*>

    internal abstract fun doInit()
}