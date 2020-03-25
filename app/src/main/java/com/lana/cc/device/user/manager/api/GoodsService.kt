package com.lana.cc.device.user.manager.api


import com.lana.cc.device.user.model.GoodsHistory
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.fragment.shop.AddGoodsRequestModel
import com.lana.cc.device.user.ui.fragment.shop.EditGoodsRequestModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface GoodsService {

    //获取兑换列表
    @POST("good/getExchangeList")
    fun getExchangeHistoryList(): Single<ResultModel<GoodsHistory>>

    //拉去商品列表
    @GET("mall")
    fun fetchGoodsList(): Single<ResultModel<List<Goods>>>

    //新增商品
    @POST("mall/goods")
    fun addGoods(@Body addGoodsRequestModel: AddGoodsRequestModel): Single<ResponseBody>

    //更改商品
    @PUT("mall/goods")
    fun editGoods(@Body editGoodsRequestModel: EditGoodsRequestModel): Single<ResponseBody>

    //删除商品
    @DELETE("mall/goods")
    fun deleteGoods(@Query ("goodsId")goodsId:Int): Single<ResponseBody>

}