package com.lana.cc.device.user.manager.base

import com.lana.cc.device.user.model.base.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset

class NetErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val resStr = response.body?.source()?.buffer?.clone()?.readString(Charset.defaultCharset())

        if (response.code in 400..503) {
            throw ServerError(
                response.code,
                response.message
            )
        } else {
            val code = resStr?.getCode()
            val message = resStr?.getMessage()
            if (code?:0 > 1000) {
                throw ApiException(code?:0, message?:"")
            }
        }
        return response
    }
}

//从json结果获取请求的 meta 状态中的 code
private fun String.getCode(): Int {
    val str = this.split(",")[0]
    val code = str.replace("{\"meta\":{\"code\":", "")
    return code.toInt()
}

//从json结果获取请求的 meta 状态中的 meassage
private fun String.getMessage(): String {
    val str = this.split(",")[1]
    val tempStr = str.replace("\"msg\":\"", "")
    val message = tempStr.replace("\"}}", "")
    return message
}


data class ServerError(val code: Int, val msg: String) : RuntimeException()
