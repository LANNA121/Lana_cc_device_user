package com.lana.cc.device.user.manager.api

import com.example.rubbishcommunity.model.api.password.ResetPasswordRequestModel
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.guide.*
import com.lana.cc.device.user.model.api.mine.UserCardResultModel
import com.lana.cc.device.user.model.api.mine.UsrProfileResp
import io.reactivex.Single
import retrofit2.http.*

interface UserService {

    //登录
    @POST("account/login")
    fun login(@Body loginModel: LoginRequestModel): Single<ResultModel<LoginResultModel>>

    //注册
    @POST("api/account/register")
    fun register(registerModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>>


    //完善用户信息
    @POST("api/account/profile/new")
    fun completeInfo(@Body completeInfoRequestModel: CompleteInfoRequestModel): Single<ResultModel<String>>

    //修改用户信息
    //修改用户昵称
    @PUT("api/account/profile/modify")
    fun editUserInfo(@Body modifyMap: HashMap<String, String>): Single<ResultModel<String>>


    //刷新用户详细信息
    @GET("api/account/profile/refresh")
    fun getUserProfile(): Single<ResultModel<UsrProfileResp>>

    //获取用户卡片信息（头像 名字 签名等基本信息）
    @GET("api/account/info/{uid}/get")
    fun getUserCard(@Path("uid") uid: String): Single<ResultModel<UserCardResultModel>>

    //注销
    @POST("api/account/logout")
    fun logout(): Single<ResultModel<String>>

    //修改密码
    @PUT("api/account/password/modify")
    fun editPassword(@Body passwordReq: ResetPasswordRequestModel): Single<ResultModel<String>>


}