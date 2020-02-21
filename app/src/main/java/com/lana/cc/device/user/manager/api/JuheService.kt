package com.lana.cc.device.user.manager.api

import com.lana.cc.device.user.model.Photography
import com.lana.cc.device.user.BuildConfig
import com.lana.cc.device.user.model.api.NewsResult
import io.reactivex.Single
import retrofit2.http.GET

interface JuheService {
	
	//获取新闻
	@GET("index?type=top&key=${BuildConfig.JUHE_APPKEY}")
	fun getNews(): Single<NewsResult>
	
	//获取杂图
	@GET("index?type=top&key=${BuildConfig.JUHE_APPKEY}")
	fun getPhotography(): Single<MutableList<Photography>>
	
}