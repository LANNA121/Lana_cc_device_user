package com.lana.cc.device.user.ui.fragment.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.guide.login.LoginRequestModel
import com.lana.cc.device.user.model.api.guide.login.LoginResultModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.util.switchThread
import org.kodein.di.generic.instance
import timber.log.Timber

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val userService by instance<UserService>()
    val userEmail = MutableLiveData("")
    val password = MutableLiveData("")
    val isLoginSuccess = MutableLiveData(false)
    val rememberPassword = MutableLiveData(false)

    fun init() {
        rememberPassword.value = SharedPrefModel.rememberPassword
        userEmail.value = SharedPrefModel.userEmail
        if (SharedPrefModel.rememberPassword)
            password.value = SharedPrefModel.password
    }

    //登录请求 RxJava
    fun login() {
        userService.login(
            LoginRequestModel(
                userEmail.value!!,
                password.value!!
            )
        )
            .switchThread()
            .catchApiError()
            .autoProgressDialog()
            .doOnSuccess {
                saveUserData(it.data)
                isLoginSuccess.postValue(true)
            }
            .bindLife()
    }

    //存user的数据到本地
    private fun saveUserData(loginResultModel: LoginResultModel?) {
        SharedPrefModel.hasLogin = true
        SharedPrefModel.userEmail = userEmail.value!!
        SharedPrefModel.password = password.value!!
        SharedPrefModel.token = loginResultModel?.token ?: ""
        SharedPrefModel.uid = loginResultModel?.uid ?: 0
        SharedPrefModel.setUserModel { role = loginResultModel?.role }
        //SharedPrefModel.setUserModel { role = ROLE_OSS }
        SharedPrefModel.rememberPassword = rememberPassword.value!!
    }

}
