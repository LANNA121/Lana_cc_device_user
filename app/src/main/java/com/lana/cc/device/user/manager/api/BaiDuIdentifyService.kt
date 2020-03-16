package com.lana.cc.device.user.manager.api

import com.lana.cc.device.user.model.api.baiduidentify.BaiduIdentifyToken
import com.lana.cc.device.user.model.api.baiduidentify.IdentifyResult
import com.lana.cc.device.user.BuildConfig
import io.reactivex.Single
import retrofit2.http.*

//百度识别API
interface BaiDuIdentifyService {
	
	//获取Token
	@POST("oauth/2.0/token?grant_type=client_credentials&client_id=${BuildConfig.BAIDU_API_KEY}&client_secret=${BuildConfig.BAIDU_SECRET_KEY}")
	fun getToken(): Single<BaiduIdentifyToken>
	
	//获取图片物体名称
	@FormUrlEncoded
	@POST("rest/2.0/image-classify/v2/advanced_general")
	fun getIdentifyThing(
		@Query("access_token")access_token:String,
		@Field("image")imageStr:String
	): Single<IdentifyResult>
	
}