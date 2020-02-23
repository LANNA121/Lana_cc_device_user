package com.lana.cc.device.user.manager.api


import com.lana.cc.device.user.model.Good
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.guide.login.LoginRequestModel
import com.lana.cc.device.user.model.api.guide.login.LoginResultModel
import io.reactivex.Single
import retrofit2.http.*

interface GoodsService {

    //登录
    @POST("good/getExchangeList")
    fun getExchangeHistoryList(): Single<ResultModel<Good>>

}