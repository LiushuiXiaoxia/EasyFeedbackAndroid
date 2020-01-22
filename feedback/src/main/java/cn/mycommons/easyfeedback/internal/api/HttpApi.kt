package cn.mycommons.easyfeedback.internal.api

import cn.mycommons.easyfeedback.api.FeedbackConfig
import cn.mycommons.easyfeedback.api.FeedbackDto
import cn.mycommons.easyfeedback.internal.api.model.UploadFileData
import cn.mycommons.easyfeedback.util.logInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.File

/**
 * HttpApi <br/>
 * Created by xiaqiulei on 2020-01-17.
 */

class HttpApi(private val config: FeedbackConfig) {

    private val jsonMediaType by lazy { MediaType.parse("application/json") }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val gson: Gson by lazy { Gson() }

    fun upload(file: File, name: String): BaseResp<UploadFileData?>? {
        logInfo("upload.file = $file")
        val type = MediaType.parse(file.name)
        val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, RequestBody.create(type, file))
                .build()

        val request = Request.Builder()
                .url("${config.uploadServer}/file/upload")
                .post(body)
                .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body()?.string()?.let {
                logInfo("body = $it")
                val value = object : TypeToken<BaseResp<UploadFileData>>() {

                }
                val resp = gson.fromJson(it, value.type) as BaseResp<UploadFileData?>?
                logInfo("resp = $resp")
                return resp
            }
        }

        return null
    }

    fun feedback(dto: FeedbackDto): String? {
        val request = Request.Builder()
                .url("${config.uploadServer}/feedback")
                .post(RequestBody.create(jsonMediaType, gson.toJson(dto)))
                .build()

        logInfo("feedback.request = $request")
        val response = client.newCall(request).execute()
        logInfo("feedback.response = $response")

        var ret: String? = null
        if (response.isSuccessful) {
            response.body()?.string()?.let {
                logInfo("body = $it")
                val value = object : TypeToken<BaseResp<String>>() {

                }
                val resp = gson.fromJson(it, value.type) as BaseResp<String?>?
                logInfo("resp = $resp")
                ret = resp?.data
            }
        }

        return ret
    }

    fun getQiniuToken(pkgName: String, platform: String): String? {
        val url = HttpUrl.parse("${config.uploadServer}/qiniu/token")!!
                .newBuilder()
                .addQueryParameter("pkgName", pkgName)
                .addQueryParameter("platform", platform)
                .build()

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        logInfo("getQiniuToken.request = $request")
        val response = client.newCall(request).execute()
        logInfo("getQiniuToken.response = $response")

        var ret: String? = null
        if (response.isSuccessful) {
            response.body()?.string()?.let {
                logInfo("body = $it")
                val value = object : TypeToken<BaseResp<String>>() {

                }
                val resp = gson.fromJson(it, value.type) as BaseResp<String?>?
                logInfo("resp = $resp")
                ret = resp?.data
            }
        }

        return ret
    }
}