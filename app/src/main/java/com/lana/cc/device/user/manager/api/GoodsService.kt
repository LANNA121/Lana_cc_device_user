package com.lana.cc.device.user.manager.api


import com.lana.cc.device.user.model.GoodsHistory
import com.lana.cc.device.user.model.api.ResultModel
import io.reactivex.Single
import retrofit2.http.*

interface GoodsService {

    //登录
    @POST("good/getExchangeList")
    fun getExchangeHistoryList(): Single<ResultModel<GoodsHistory>>

}