package cn.mycommons.easyfeedback.internal.api.req

/**
 * GetQiniuTokenReq <br/>
 * Created by xiaqiulei on 2020-01-21.
 */

class GetQiniuTokenReq {

    var pkgName: String? = null

    var platform: String? = null

    override fun toString(): String {
        return "GetQiniuTokenReq(pkgName=$pkgName, platform=$platform)"
    }
}