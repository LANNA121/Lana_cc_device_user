package com.lana.cc.device.user.manager.api

import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.shop.ChangeBillStatusRequestModel
import com.lana.cc.device.user.model.api.shop.ExchangeGoodsRequestModel
import com.lana.cc.device.user.model.api.shop.FetchAccountBookHistoryResultModel
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.fragment.mine.ExChangeGoodsHistoryResultModel
import com.lana.cc.device.user.ui.fragment.mine.ExchangeGoodsHistoryModel
import com.lana.cc.device.user.ui.fragment.shop.AddGoodsRequestModel
import com.lana.cc.device.user.ui.fragment.shop.EditGoodsRequestModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface GoodsService {

    //拉去商品列表
    @GET("mall/goods")
    fun fetchGoodsList(): Single<ResultModel<List<Goods>>>

    //新增商品
    @POST("mall/goods")
    fun addGoods(@Body addGoodsRequestModel: AddGoodsRequestModel): Single<ResponseBody>

    //更改商品
    @PUT("mall/goods")
    fun editGoods(@Body editGoodsRequestModel: EditGoodsRequestModel): Single<ResponseBody>

    //删除商品
    @DELETE("mall/goods")
    fun deleteGoods(@Query("goodsId") goodsId: Int): Single<ResponseBody>

    //获取积分历史
    @GET("book/history")
    fun fetchAccountBookHistoryList(@Query("uid") uid: Int): Single<ResultModel<FetchAccountBookHistoryResultModel>>

    //获取所有用户兑换商品历史
    @GET("mall/bill/all")
    fun fetchAllUsersExchangeGoodsHistoryList(): Single<ResultModel<ExChangeGoodsHistoryResultModel>>

    //根据uid获取兑换商品历史
    @GET("mall/bill")
    fun fetchExchangeGoodsHistoryList(@Query("uid") uid: Int = SharedPrefModel.uid): Single<ResultModel<ExChangeGoodsHistoryResultModel>>

    //兑换商品
    @POST("mall")
    fun exchangeGoods(@Body exchangeGoodsRequestModel: ExchangeGoodsRequestModel): Single<ResponseBody>

    //更改订单状态
    @PUT("mall/bill/handler")
    fun changeBillStatus(@Body changeBillStatusRequestModel: ChangeBillStatusRequestModel): Single<ResponseBody>

}