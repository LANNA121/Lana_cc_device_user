package com.lana.cc.device.user.manager.api


import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.news.NewsListModel
import io.reactivex.Single
import retrofit2.http.*

interface NewsService {

    //获取新闻列表
    @GET("news")
    fun getNews(): Single<ResultModel<NewsListModel>>


}