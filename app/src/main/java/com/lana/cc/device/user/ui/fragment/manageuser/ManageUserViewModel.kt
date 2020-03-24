package com.lana.cc.device.user.ui.fragment.manageuser

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.model.api.mine.UpdateUserModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.fragment.mine.upLoadImage
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import org.kodein.di.generic.instance
import java.io.File

class ManageUserViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()
    val isRefreshing = MutableLiveData(false)
    var oldUserList = emptyList<Profile>()
    val userList = MutableLiveData(emptyList<Profile>())
    val searchKey = MutableLiveData("")
    val avatarFile = MutableLiveData<File>()


    fun fetchUsers() {
        userService.fetchUserList()
            .doOnApiSuccess {
                userList.postValue(it.data?.userProfileRspList)
                oldUserList = it.data?.userProfileRspList ?: emptyList()
            }
    }

    //更改 昵称 生日 签名
    fun editUserProfile(
        birthLong: Long,
        nickName: String,
        signature: String
    ) {
        fun editInfo(avatarPath: String = "") = userService.updateUserProfile(
            UpdateUserModel(
                birthLong,
                nickName,
                signature,
                avatarPath
            )
        )

        val single = if (avatarFile.value == null) editInfo()
        else {
            //上传头像
            userService.upLoadImage(avatarFile.value)
                ?.flatMap {
                    //修改信息
                    editInfo(it.data?.imagePath ?: "")
                }
        }
        single?.doOnApiSuccess { avatarFile.postValue(null) }
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