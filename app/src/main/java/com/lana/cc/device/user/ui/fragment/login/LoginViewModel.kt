package com.lana.cc.device.user.ui.fragment.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.guide.LoginOrRegisterRequestModel
import com.lana.cc.device.user.model.api.guide.LoginRequestModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import jp.co.nikkei.t21.android.util.switchThread
import org.kodein.di.generic.instance

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val userService by instance<UserService>()
    val userEmail = MutableLiveData("")
    val password = MutableLiveData("")
    val isLoginSuccess = MutableLiveData(false)

    fun init() {
        userEmail.value = SharedPrefModel.userEmail
        if (SharedPrefModel.rememberPassword)
            password.value = SharedPrefModel.password
    }

    fun login() {
        userService.login(
            LoginRequestModel(
                userEmail.value!!,
                password.value!!
            )
        )
            .switchThread()
            .catchApiError()
            .doOnSuccess {
                isLoginSuccess.postValue(true)
            }
            .bindLife()
    }

}
