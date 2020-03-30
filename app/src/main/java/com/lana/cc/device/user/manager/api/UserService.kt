package com.lana.cc.device.user.manager.api

import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.AddressRequestInfo
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.guide.login.LoginRequestModel
import com.lana.cc.device.user.model.api.guide.login.LoginResultModel
import com.lana.cc.device.user.model.api.guide.register.RegisterRequestModel
import com.lana.cc.device.user.model.api.guide.register.UploadAvatarResultModel
import com.lana.cc.device.user.model.api.manageuser.EditPasswordRequestModel
import com.lana.cc.device.user.model.api.manageuser.EditUserRoleModel
import com.lana.cc.device.user.model.api.manageuser.FetchAllUserProfileResultModel
import com.lana.cc.device.user.model.api.mine.UpdateUserModel
import com.lana.cc.device.user.model.api.shop.FetchAddressListResultModel
import com.lana.cc.device.user.model.api.test.ChangeRedeemRequestModel
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface UserService {

    @GET("/")
    fun test(): Single<String>

    //登录
    @POST("account/login")
    fun login(@Body loginModel: LoginRequestModel): Single<ResultModel<LoginResultModel>>

    //注册
    @POST("account/register")
    fun register(@Body registerModel: RegisterRequestModel): Single<ResultModel<String>>

    //上传头像
    @Multipart
    @POST("common/upload")
    fun upLoadImage(@Part file: MultipartBody.Part): Single<ResultModel<UploadAvatarResultModel>>

    //获取用户信息
    @GET("account/profile")
    fun getUserProfile(@Query("uid") uid: Int): Single<ResultModel<Profile>>

    //更改用户信息
    @PUT("account/modify")
    fun updateUserProfile(@Body updateUserModel: UpdateUserModel): Single<ResultModel<String?>>

    //删除用户
    @DELETE("account/profile")
    fun deleteUser(@Query("uid") uid: Int?): Single<ResponseBody>

    //拉取所有user
    @GET("account/all/user")
    fun fetchUserList(): Single<ResultModel<FetchAllUserProfileResultModel>>

    //更改用户权限
    @POST("account/role")
    fun editRole(@Body editUserRoleModel: EditUserRoleModel): Single<ResponseBody>

    //更改用户密码
    @PUT("account/modify/password")
    fun editPassword(@Body editPasswordRequestModel: EditPasswordRequestModel): Single<ResponseBody>

    //获取积分数据
    @GET("book/coins")
    fun fetchCoins(): Single<ResultModel<Int>>

    //换取积分
    @POST("book/redeem")
    fun changeRedeem(@Body changeRedeemRequestModel: ChangeRedeemRequestModel): Single<ResponseBody>

    //新增收获地址
    @POST("account/address")
    fun createNewAddress(@Body addressRequestInfo: AddressRequestInfo): Single<ResponseBody>

    //新增收获地址
    @DELETE("account/address")
    fun deleteAddress(
        @Query("addressId") addressId: Int,
        @Query("uid") uid: Int = SharedPrefModel.uid
    ): Single<ResponseBody>

    //获取收获地址
    @GET("account/address")
    fun fetchAddressList(@Query("uid") uid: Int = SharedPrefModel.uid): Single<ResultModel<FetchAddressListResultModel>>

}