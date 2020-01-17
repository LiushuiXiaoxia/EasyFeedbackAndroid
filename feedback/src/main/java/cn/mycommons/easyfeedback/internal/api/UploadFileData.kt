package cn.mycommons.easyfeedback.internal.api

class UploadFileData {
    val fileName: String? = null
    val downloadUrl: String? = null
    val type: String? = null
    val size: Long = 0

    override fun toString(): String {
        return "UploadFileData(fileName=$fileName, downloadUrl=$downloadUrl, type=$type, size=$size)"
    }
}