package com.lana.cc.device.user.ui.fragment.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import jp.co.nikkei.t21.android.manager.api.base.ApiClient
import com.lana.cc.device.user.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class RegisterViewModel(application: Application) : BaseViewModel(application) {

    val userName = MutableLiveData("")
    val password = MutableLiveData("")
    val isLoginSuccess = MutableLiveData(false)


    fun init() {
        userName.value = SharedPrefModel.userEmail
/*        SharedPrefModel.autoLogin.run {
            isAutoLoginEnabled.value = this
            if (this) {
                password.value = SharedPrefModel.password
                if (!isBackFromHome && SharedPrefModel.hasLogin) {
                    //checkAndLogin()
                }
            }
        }*/
    }

/*    fun checkAndLogin() {
        getLoginSingle(
            userName.value!!, password.value!!,
            chargeCode.value!!
        )
            .onErrorResumeNext { error ->
                when {
                    error.isUnableError() ->
                        return@onErrorResumeNext getOnlineSignUpUserInfoSingle()

                    error.isCompulsionError() ->
                        return@onErrorResumeNext getForceLoginSingle(
                            userName.value!!,
                            password.value!!,
                            chargeCode.value!!
                        )
                    else -> return@onErrorResumeNext Single.error(error)
                }
            }
            .doOnSuccess {
                MainApplication.nowUserId = it.CMN0F13!!.userId!!
                saveUserData()
            }
            .doOnSubscribe {
                showCenterView.postValue(false)
            }
            .doOnLoginOver()
            .switchThread()
            .autoProgressDialog()
            .catchApiError()
            .bindLife()
    }

    private fun getLoginSingle(
        userId: String,
        password: String,
        chargeCode: String
    ): Single<LoginModel> = cmn0Service.login(userId, password, chargeCode)

    private fun <T> Single<T>.doOnLoginOver(){
        return flatMap {
            cmN5Service.uiSetting()
        }.flatMap { uiSettingModel ->
            //get UiSetting success
            val priceShow = uiSettingModel.userSetting?.priceShow
            SharedPrefModel.getUserModel().priceShow =
                priceShow != null && priceShow.isNotEmpty()
            cmN5Service.userSetting()
        }.retry { error -> return@retry error.isUnusableError() }
            .doOnSuccess { userSettingModel ->
                //getUserSetting success
                SharedPrefModel.setUserModel {
                    confirmDialogDisplay =
                        "Y" == userSettingModel.userSetting?.confirmInsertFlg
                    priceContentDisplay = "Y" == userSettingModel.userSetting?.priceShow
                }
                isLoginSuccess.postValue(true)
            }.doOnError {
                showCenterView.postValue(true)
            }
    }

    */


}
