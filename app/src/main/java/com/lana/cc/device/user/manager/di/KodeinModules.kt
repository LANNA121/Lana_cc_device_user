package com.lana.cc.device.user.manager.di


import com.lana.cc.device.user.BuildConfig
import com.lana.cc.device.user.manager.api.*
import com.lana.cc.device.user.manager.base.BaiduIdentifyClient
import com.lana.cc.device.user.manager.base.ApiClient
import com.lana.cc.device.user.manager.base.HeaderInterceptor
import com.lana.cc.device.user.manager.base.NetErrorInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.TimeUnit

val apiModule = Kodein.Module("api") {

    //api
    bind<ApiClient>() with singleton { provideApiClient() }

    bind<UserService>() with singleton { instance<ApiClient>().createService(UserService::class.java) }

    bind<NewsService>() with singleton { instance<ApiClient>().createService(NewsService::class.java) }

    bind<GoodsService>() with singleton { instance<ApiClient>().createService(GoodsService::class.java) }

    bind<RubbishService>() with singleton { instance<ApiClient>().createService(RubbishService::class.java) }


    //百度识别
    bind<BaiduIdentifyClient>() with singleton { provideBaiDuIdentifyClient() }

    bind<BaiDuIdentifyService>() with singleton {
        instance<BaiduIdentifyClient>().createService(
            BaiDuIdentifyService::class.java
        )
    }

}

/*fun databaseModule(app: Application) = Kodein.Module("database") {
    bind<AppDatabase>() with singleton {
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME_APP
        ).addCallback(AppDatabase.CALLBACK)
            .build()
    }

    bind<ArticleSearchHistoryDao>() with provider { instance<AppDatabase>().articleSearchHistoryDao() }

}*/


fun provideApiClient(): ApiClient {
    val client = ApiClient.Builder()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    client.okBuilder
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(NetErrorInterceptor())
/*        .addInterceptor(AddCookiesInterceptor())
        .addInterceptor(ReceivedCookiesInterceptor())*/
        .readTimeout(30, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }

    return client.build(baseUrl = BuildConfig.BASE_URL)
}

fun provideBaiDuIdentifyClient(): BaiduIdentifyClient {
    val client = BaiduIdentifyClient.Builder()
    val logInterceptor = HttpLoggingInterceptor()
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY

    client.okBuilder
        //.addInterceptor(HeaderInterceptor())
        .addInterceptor(NetErrorInterceptor())
        .apply {
            if (BuildConfig.DEBUG)
                addInterceptor(logInterceptor)
        }
        .readTimeout(10, TimeUnit.SECONDS)

    return client.build()
}
