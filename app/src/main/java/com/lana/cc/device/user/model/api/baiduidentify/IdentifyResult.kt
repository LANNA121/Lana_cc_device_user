package com.lana.cc.device.user.model.api.baiduidentify

import com.squareup.moshi.Json

data class IdentifyResult(
	@Json(name = "log_id")
	val logId:Long,
	@Json(name = "result_num")
	val resultNum:Int,
	@Json(name = "result")
	val result:MutableList<Thing>
)

data class Thing(
	@Json(name = "score")
	val score:Double,
	@Json(name = "root")
	val root:String,
	@Json(name = "keyword")
	val keyword:String

)
