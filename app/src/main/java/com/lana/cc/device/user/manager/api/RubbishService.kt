package com.lana.cc.device.user.manager.api

import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.news.QuestionListModel
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.model.api.search.ClassificationRequestModel
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface RubbishService {
	
	//根据物品名称搜索分类
	@GET("common/tools/{searchKey}/search")
	fun searchClassByName(@Path("searchKey") searchKey: String): Single<ResultModel<MutableList<SearchKeyConclusion>>>

	//根据sortId搜索分类信息
	@GET("common/tools/{classNum}/categories")
	fun searchCategoryInfo(@Path("classNum") classNum: Int): Single<ResultModel<Category>>

	//获取分类题卡
	@GET("game/question")
	fun fetchQuestion(): Single<ResultModel<QuestionListModel>>

	//增加分类信息
	@POST("common/tools/categories")
	fun addCategory(@Body classificationRequestModel: ClassificationRequestModel): Single<ResponseBody>

	//修改分类信息
	@PUT("common/tools/categories")
	fun editCategory(@Body classificationRequestModel: ClassificationRequestModel): Single<ResponseBody>

	//删除分类信息
	@DELETE("common/tools/categories")
	fun deleteCategory(
		@Query ("classKey")classKey:String
	): Single<ResponseBody>

}