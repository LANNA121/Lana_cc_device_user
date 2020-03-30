package com.lana.cc.device.user.ui.fragment.manageuser

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.model.api.guide.register.ROLE_OSS
import com.lana.cc.device.user.model.api.guide.register.ROLE_USER
import com.lana.cc.device.user.model.api.manageuser.EditPasswordRequestModel
import com.lana.cc.device.user.model.api.manageuser.EditUserRoleModel
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.model.api.mine.UpdateUserModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.base.Event
import com.lana.cc.device.user.ui.base.LiveEvent
import com.lana.cc.device.user.ui.fragment.mine.ExchangeGoodsHistoryModel
import com.lana.cc.device.user.ui.fragment.mine.upLoadImage
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import org.kodein.di.generic.instance
import java.io.File

class ManageUserViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()
    private val goodsService by instance<GoodsService>()
    val isRefreshing = MutableLiveData(false)
    var oldUserList = emptyList<Profile>()
    val userList = MutableLiveData(emptyList<Profile>())
    val searchKey = MutableLiveData("")
    val avatarFile = MutableLiveData<File>()
    val removeSuccess = LiveEvent<Event<Boolean>>()

    //获取用户信息列表
    fun fetchUsers() {
        userService.fetchUserList()
            .doOnApiSuccess {
                val profileList = it.data?.userProfileRspList
                val ossList =
                    profileList?.filter { profile -> profile.role == ROLE_OSS }
                        ?.sortedBy { profile -> profile.createTime }
                        ?: emptyList() //管理员列表
                val commonUserList =
                    profileList?.filter { profile -> profile.role == ROLE_USER }
                        ?.sortedBy { profile -> profile.createTime }
                        ?: emptyList() //普通用户列表
                val resultList = listOf<Profile>().toMutableList()
                    .apply {
                        toMutableList()
                        addAll(ossList)
                        addAll(commonUserList)
                    }.toList()

                userList.postValue(resultList)
                oldUserList = resultList
            }
    }

    //获取用户的兑换历史
    fun fetchUserExchangeHistory(uid: Int, onLoaded: (List<ExchangeGoodsHistoryModel>) -> Unit) {
        goodsService.fetchExchangeGoodsHistoryList(uid)
            .doOnApiSuccess {
                onLoaded(it.data?.bills ?: emptyList())
            }
    }

    //更改 昵称 生日 签名 密码
    fun editUserProfile(
        uid: Int?,
        birthLong: Long,
        nickName: String,
        signature: String,
        password: String,
        role: String,
        action: () -> Unit
    ) {
        fun updateUserProfile(avatarPath: String = "") =
            userService.updateUserProfile(
                UpdateUserModel(
                    birthLong,
                    nickName,
                    signature,
                    avatarPath,
                    uid = uid
                )
            )


        fun editInfo(avatarPath: String = "") = if (password.isNotEmpty()) {
            userService.editPassword(
                EditPasswordRequestModel(
                    uid ?: 0,
                    password
                )
            ).flatMap {
                updateUserProfile(avatarPath)
            }
        } else {
            updateUserProfile(avatarPath)
        }

        val single = if (avatarFile.value == null) editInfo()
        else {
            //上传头像
            userService.upLoadImage(avatarFile.value)
                ?.flatMap {
                    //修改信息
                    editInfo(it.data?.imagePath ?: "")
                }
        }
        single?.flatMap {
            userService.editRole(
                EditUserRoleModel(
                    uid = uid,
                    role = role
                )
            )
        }?.doOnApiSuccess {
            //将此页面的全局的头像file文件置为空，避免进入其他用户弹窗时还没有头像的时候 把之前选择过的头像文件读取到
            avatarFile.postValue(null)
            action.invoke()
        }
    }

    fun removeUser(uid: Int?) {
        userService.deleteUser(uid)
            .doOnApiSuccess {
                removeSuccess.postValue(Event(true))
                fetchUsers()
            }
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