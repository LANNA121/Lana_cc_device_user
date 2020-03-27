package com.lana.cc.device.user.ui.fragment.mine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.model.api.ResultModel
import com.lana.cc.device.user.model.api.guide.register.UploadAvatarResultModel
import com.lana.cc.device.user.model.api.mine.UpdateUserModel
import com.lana.cc.device.user.model.api.shop.ChangeBillStatusRequestModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.utils.getImageFromServer
import com.lana.cc.device.user.util.getAgeByBirth
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.generic.instance
import java.io.File
import java.util.*

class MineViewModel(application: Application) : BaseViewModel(application) {

    val profile = MutableLiveData(Profile.getDefault())
    val age = MutableLiveData("0岁")
    private val userService by instance<UserService>()
    private val goodsService by instance<GoodsService>()
    val avatarFile = MutableLiveData<File>()
    val avatar = MutableLiveData<String>()
    val exchangeGoodsList = MutableLiveData(emptyList<ExchangeGoodsHistoryModel>())
    val isRefreshing = MutableLiveData(false)

    fun fetchUserProfile(uid: Int = SharedPrefModel.uid) {
        userService.getUserProfile(uid)
            .doOnGetProfileSuccess()
    }

    fun fetchExchangeGoodsHistory() {
        goodsService.fetchExchangeGoodsHistoryList()
            .doOnApiSuccess {
                exchangeGoodsList.postValue(it.data?.bills)
            }
    }

    fun finishBill(billId: String) {
        goodsService.changeBillStatus(
            ChangeBillStatusRequestModel(
                billId = billId,
                billStatus = 3
            )
        ).doOnApiSuccess {
            fetchExchangeGoodsHistory()
        }
    }

    private fun Single<ResultModel<Profile>>.doOnGetProfileSuccess() =
        doOnApiSuccess {
            profile.postValue(it.data)
            //avatar.postValue("${BuildConfig.BASE_URL}/image/${it.data?.avatar}")
            avatar.postValue(getImageFromServer(it.data?.avatar))
            age.postValue(getAgeByBirth(Date(it.data?.birthday ?: 0.toLong())).toString() + "岁")
        }


    //上传头像,并修改头像
    fun uploadAvatar() {
        userService.upLoadImage(avatarFile.value)
            ?.flatMap {
                //上传成功就更新本地头像
                avatar.postValue(getImageFromServer(it.data?.imagePath))
                //调用修改头像api
                userService.updateUserProfile(
                    UpdateUserModel(
                        0, "", "", it.data?.imagePath
                    )
                )
            }?.doOnApiSuccess {}
    }

    //更改 昵称 生日 签名
    fun updateUserProfile(birthLong: Long, nickName: String, signature: String) {
        userService.updateUserProfile(
            UpdateUserModel(
                birthLong, nickName, signature, ""
            )
        ).flatMap {
            userService.getUserProfile(SharedPrefModel.uid)
        }.doOnGetProfileSuccess()
    }

    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread()
            .doOnSuccess {
                action?.invoke(it)
            }
            .doOnSubscribe { isRefreshing.postValue(true) }
            .doOnSuccess { isRefreshing.postValue(false) }
            .doOnError { isRefreshing.postValue(false) }
            .catchApiError()
            .bindLife()
    }
}

fun UserService.upLoadImage(file: File?): Single<ResultModel<UploadAvatarResultModel>>? {
    return if (file != null) {
        val photoRequestBody =
            RequestBody.create("image/png".toMediaTypeOrNull(), file)
        val photo = MultipartBody.Part.createFormData(
            "imageFile",
            file.name,
            photoRequestBody
        )
        upLoadImage(
            photo
        ).onErrorResumeNext {
            Single.just(ResultModel(ResultModel.Meta(1999, ""), UploadAvatarResultModel("")))
        }
    } else null
}