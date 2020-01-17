package cn.mycommons.easyfeedback.internal.api

class BaseResp<T> {

    var code: Int = 0
    var data: T? = null
    var msg: String? = null

    override fun toString(): String {
        return "BaseResp(code=$code, data=$data, msg=$msg)"
    }

    fun isSuccess(): Boolean = code == 200
}