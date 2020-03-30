package com.lana.cc.device.user.ui.fragment.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.BuildConfig
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.guide.login.LoginRequestModel
import com.lana.cc.device.user.model.api.guide.login.LoginResultModel
import com.lana.cc.device.user.model.api.guide.register.RegisterRequestModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.utils.getImageFromServer
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.generic.instance
import java.io.File

class RegisterViewModel(application: Application) : BaseViewModel(application) {

    val userEmail = MutableLiveData("")
    val password = MutableLiveData("")
    val rePassword = MutableLiveData("")
    val gender = MutableLiveData("M")
    val nickName = MutableLiveData("")
    val signature = MutableLiveData("")
    val birthLong = MutableLiveData(0.toLong())
    val avatar = MutableLiveData("")
    private val avatarRelative = MutableLiveData("")
    val avatarFile = MutableLiveData<File>()
    val isLoginSuccess = MutableLiveData(false)
    private val userService by instance<UserService>()

    //上传头像
    fun uploadAvatar() {
        if (avatarFile.value != null) {
            val photoRequestBody =
                RequestBody.create("image/png".toMediaTypeOrNull(), avatarFile.value!!)
            val photo = MultipartBody.Part.createFormData(
                "imageFile",
                avatarFile.value!!.name,
                photoRequestBody
            )
            userService.upLoadImage(
                photo
            ).doOnApiSuccess {
                avatarRelative.postValue(it.data?.imagePath)
                avatar.postValue(
                    getImageFromServer(it.data?.imagePath))
            }
        }
    }

    //注册
    fun registerAndLogin() {
        userService.register(
            RegisterRequestModel(
                userEmail.value!!,
                password.value!!,
                birthLong.value!!,
                nickName.value!!,
                gender.value!!,
                signature.value!!,
                avatarRelative.value!!
            )
        ).flatMap {
            //转换为登录请求
            userService.login(
                LoginRequestModel(
                    userEmail.value!!,
                    password.value!!
                )
            )
        }.doOnApiSuccess {
            saveUserData(it.data)
            isLoginSuccess.postValue(true)
        }

    }

    //对Single类的扩展，专用于处理切换线程，展示一个圈，做成功后的操作，操作就是下面的action
    //action的类型 是 一个操作 输入泛型T类型 返回Unit空（即没有返回）
    //只有在调用此方法时，T才被确定，例如上面的 userService.register调用时，T就是ResultModel<Unit>，
    //按住ctrl 对上面代码中的变量或者方法（这里是register）点左键 可以查看定义 看到类型是ResultModel<Unit>
    //不清楚建议网上查看泛型
    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread(
        ).autoProgressDialog()
            .doOnSuccess {
                action?.invoke(it)
            }
            .catchApiError()
            .bindLife()
    }

    //存user的数据到本地
    private fun saveUserData(loginResultModel: LoginResultModel?) {
        SharedPrefModel.hasLogin = true
        SharedPrefModel.userEmail = userEmail.value!!
        SharedPrefModel.password = password.value!!
        SharedPrefModel.token = loginResultModel?.token?:""
        SharedPrefModel.uid = loginResultModel?.uid?:0

    }

}
