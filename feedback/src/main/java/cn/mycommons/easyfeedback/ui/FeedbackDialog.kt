package cn.mycommons.easyfeedback.ui

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.appcompat.app.AlertDialog
import cn.mycommons.easyfeedback.R
import cn.mycommons.easyfeedback.internal.FbUtil
import cn.mycommons.easyfeedback.util.logError
import cn.mycommons.easyfeedback.util.logInfo
import cn.mycommons.easyfeedback.util.randomImageName
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * FeedbackDialog <br/>
 * Created by xiaqiulei on 2020-01-12.
 */
class FeedbackDialog(var activity: Activity, var imageFile: File? = null) {

    fun showDialog() {
        AlertDialog.Builder(activity)
                .setTitle(R.string.fb_dialog_title)
                .setMessage(R.string.fb_dialog_msg)
                .setPositiveButton(R.string.fb_dialog_ok) { _, _ ->
                    if (imageFile == null) {
                        genCaptureScreen()
                    } else {
                        copyImage()
                    }
                    gotoEditActivity()
                }
                .setNegativeButton(R.string.fb_dialog_cancel, null)
                .create()
                .show()
    }

    private fun genCaptureScreen() {
        val dView: View = activity.window.decorView
        dView.isDrawingCacheEnabled = true
        dView.buildDrawingCache()
        val bitmap: Bitmap = Bitmap.createBitmap(dView.drawingCache)
        try {
            // 图片文件路径
            imageFile = File(activity.externalCacheDir, randomImageName())

            logInfo("genCaptureScreen.file = %s", imageFile)

            val os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            logError("genCaptureScreen fail", e)
        }
    }

    private fun copyImage() {
        imageFile?.let {
            if (it.absoluteFile.absolutePath.contains(FbUtil.context().packageName)) {
                // 复制到当前App文件夹
            }
        }
    }

    private fun gotoEditActivity() {
        imageFile?.let {
            EditImageActivity.gotoEdit(activity, it.absolutePath)
        }
    }
}