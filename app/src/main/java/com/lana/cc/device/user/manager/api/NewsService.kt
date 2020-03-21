package com.lana.cc.device.user.manager.api


import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.news.AddNewsRequestModel
import com.lana.cc.device.user.model.api.news.EditNewsRequestModel
import com.lana.cc.device.user.model.api.news.NewsListModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface NewsService {

    //获取新闻列表
    @GET("news")
    fun getNews(): Single<ResultModel<NewsListModel>>

    //添加新闻
    @POST("news")
    fun addNews(@Body addNewsRequestModel: AddNewsRequestModel): Single<ResponseBody>

    //修改新闻
    @PUT("news")
    fun editNews(@Body editNewsRequestModel: EditNewsRequestModel): Single<ResponseBody>

    //删除新闻
    @DELETE("news")
    fun deleteNews(@Query ("newsId")newsId:Long): Single<ResponseBody>


}