package com.lana.cc.device.user.manager.api


import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import io.reactivex.Single
import retrofit2.http.*

interface RubbishService {
	

	
	//根据物品名称搜索分类
	@GET("common/tools/{searchKey}/search")
	fun searchClassByName(@Path("searchKey") searchKey: String): Single<ResultModel<MutableList<SearchKeyConclusion>>>
	
	
	//根据sortId搜索分类信息
	@GET("common/tools/{classNum}/categories")
	fun searchCategoryInfo(@Path("classNum") classNum: Int): Single<ResultModel<Category>>
	


}