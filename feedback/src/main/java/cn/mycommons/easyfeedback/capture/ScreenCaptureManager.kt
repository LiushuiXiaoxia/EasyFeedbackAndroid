package cn.mycommons.easyfeedback.capture

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
import cn.mycommons.easyfeedback.internal.FbUtil
import cn.mycommons.easyfeedback.util.logError
import cn.mycommons.easyfeedback.util.logInfo
import java.util.*


/**
 * MediaManager <br/>
 * Created by xiaqiulei on 2020-01-12.
 */

object ScreenCaptureManager {

    private val KEYWORDS = arrayOf(
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    )

    /**
     * 读取媒体数据库时需要读取的列
     **/
    private val MEDIA_PROJECTIONS = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN
    )

    private val context: Context = FbUtil.context()
    private var handlerThread: HandlerThread = HandlerThread("Screenshot_Observer")
    private var handler: Handler
    private val internalObserver: ContentObserver
    private val externalObserver: ContentObserver

    var onCaptureChangeCallback: OnCaptureChangeCallback? = null

    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        internalObserver = MediaContentObserver(INTERNAL_CONTENT_URI, handler)
        externalObserver = MediaContentObserver(EXTERNAL_CONTENT_URI, handler)
    }

    fun start() {
        // 添加监听
        context.contentResolver.apply {
            registerContentObserver(INTERNAL_CONTENT_URI, false, internalObserver)
            registerContentObserver(EXTERNAL_CONTENT_URI, false, externalObserver)
        }
    }

    fun stop() {
        context.contentResolver.apply {
            unregisterContentObserver(internalObserver)
            unregisterContentObserver(externalObserver)
        }
    }

    private fun handleChange(contentUri: Uri) {
        var cursor: Cursor? = null

        try { // 数据改变时查询数据库中最后加入的一条数据
            cursor = context.contentResolver.query(
                    contentUri,
                    MEDIA_PROJECTIONS,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            )
            if (cursor == null || !cursor.moveToFirst()) {
                return
            }
            // 获取各列的索引
            val dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            // val dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)

            // 获取行数据
            val data = cursor.getString(dataIndex)
            // val dateTaken = cursor.getLong(dateTakenIndex)

            // 处理获取到的第一行数据
            handleMediaRowData(data)
        } catch (e: Exception) {
            logError("handleChange error", e)
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
    }

    /**
     * 处理监听到的资源
     */
    private fun handleMediaRowData(data: String) {
        if (checkScreenShot(data)) {
            logInfo("handleMediaRowData = $data")
            onCaptureChangeCallback?.onCapture(data)
        } else {
            logInfo("Not screenshot event")
        }
    }

    /**
     * 判断是否是截屏
     */
    private fun checkScreenShot(data: String): Boolean {
        val d = data.toLowerCase(Locale.getDefault())
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (work in KEYWORDS) {
            if (d.contains(work)) {
                return true
            }
        }
        return false
    }

    class MediaContentObserver(private val contentUri: Uri,
                               handler: Handler) : ContentObserver(handler) {

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)

            handleChange(contentUri)
        }
    }

    interface OnCaptureChangeCallback {

        fun onCapture(path: String?)
    }
}
