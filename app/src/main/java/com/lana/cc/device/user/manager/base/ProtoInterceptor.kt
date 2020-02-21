package com.lana.cc.device.user.manager.base

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import android.util.Base64
import android.util.Base64.NO_WRAP

class ProtoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.header("Content-Type") == "application/x-protobuf;charset=UTF-8") {
            return response.newBuilder().body(
                ResponseBody.create(
                    response.body?.contentType(),
                    Base64.decode(response.body?.bytes(), NO_WRAP)
                )
            ).build()
        }
        return response
    }
}
