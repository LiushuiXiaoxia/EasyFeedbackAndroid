package cn.mycommons.easyfeedback.internal.plugin.impl

import cn.mycommons.easyfeedback.feedback_timber.FbTimberHelper
import cn.mycommons.easyfeedback.internal.FbUtil
import cn.mycommons.easyfeedback.internal.plugin.BasePlugin
import java.io.File

/**
 * TimberPlugin <br/>
 * Created by xiaqiulei on 2020-01-12.
 */
object TimberPlugin : BasePlugin("TimberPlugin") {

    override fun pluginClass(): Class<*> {
        return FbTimberHelper.javaClass
    }

    override fun doInit() {
        FbTimberHelper.init(FbUtil.context())
    }

    fun archivingFile(): File? {
        if (enable) {
            return FbTimberHelper.archivingFile()
        }
        return null
    }
}