package com.lana.cc.device.user.manager.base

import com.lana.cc.device.user.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

open class BaiduIdentifyClient(
    private val retrofit: Retrofit,
    val okHttpClient: OkHttpClient
) {

    fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    class Builder(
        //val apiAuthorizations: MutableMap<String, Interceptor> = LinkedHashMap(),
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder(),
        private val adapterBuilder: Retrofit.Builder = Retrofit.Builder()
    ) {
        fun build(url: String? = null): BaiduIdentifyClient {
            val baseUrl = url ?: BuildConfig.BAIDU_IDENTIFY_URL
            adapterBuilder
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(globalMoshi).asLenient())

            val client = okBuilder.addInterceptor { chain ->
                val origin = chain.request()
                val request = origin
                    .newBuilder()
                    .header("Accept", "application/json;charset=UTF-8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(origin.method, origin.body)
                    .build()
                chain.proceed(request)
            }.build()

            val retrofit = adapterBuilder.client(client).build()
            return BaiduIdentifyClient(retrofit, client)
        }
    }

}