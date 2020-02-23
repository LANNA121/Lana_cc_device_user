package com.lana.cc.device.user.manager.api

import com.lana.cc.device.user.model.api.Profile
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.guide.*
import com.lana.cc.device.user.model.api.guide.login.LoginRequestModel
import com.lana.cc.device.user.model.api.guide.login.LoginResultModel
import com.lana.cc.device.user.model.api.guide.register.RegisterRequestModel
import com.lana.cc.device.user.model.api.guide.register.UploadAvatarResultModel
import com.lana.cc.device.user.model.api.mine.UpdateUserModel
import com.lana.cc.device.user.model.api.news.NewsListModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {

    //登录
    @POST("account/login")
    fun login(@Body loginModel: LoginRequestModel): Single<ResultModel<LoginResultModel>>

    //注册
    @POST("account/register")
    fun register(@Body registerModel: RegisterRequestModel): Single<ResultModel<String>>

    //上传头像
    @Multipart
    @POST("common/upload")
    fun upLoadAvatar(@Part file: MultipartBody.Part): Single<ResultModel<UploadAvatarResultModel>>

    //获取用户信息
    @GET("account/profile")
    fun getUserProfile(@Query("uid") uid: Int): Single<ResultModel<Profile>>

    //更改用户头像
    @PUT("account/modify")
    fun updateUserProfile(@Body updateUserModel: UpdateUserModel): Single<ResultModel<String?>>

    //获取新闻列表
    @GET("news")
    fun getNews(): Single<ResultModel<NewsListModel>>

    //完善用户信息
    @POST("api/account/profile/new")
    fun completeInfo(@Body completeInfoRequestModel: CompleteInfoRequestModel): Single<ResultModel<String?>>

    //修改用户信息
    //修改用户昵称
    @PUT("api/account/profile/modify")
    fun editUserInfo(@Body modifyMap: HashMap<String, String>): Single<ResultModel<String>>


}