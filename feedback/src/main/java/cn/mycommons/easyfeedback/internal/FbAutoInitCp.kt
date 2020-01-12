package cn.mycommons.easyfeedback.internal

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import cn.mycommons.easyfeedback.FeedbackHelper

/**
 * 自动初始化
 * FbAutoInitCp <br/>
 * Created by xiaqiulei on 2020-01-11.
 */
class FbAutoInitCp : ContentProvider() {

    override fun onCreate(): Boolean {
        FeedbackHelper.context = context!!
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri,
                       projection: Array<out String>?,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor? {
        return null
    }


    override fun update(uri: Uri,
                        values: ContentValues?,
                        selection: String?,
                        selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }
}