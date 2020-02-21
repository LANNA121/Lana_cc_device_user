package com.lana.cc.device.user.ui.activity

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.ui.base.Event
import com.lana.cc.device.user.ui.base.LiveEvent
import com.lana.cc.device.user.ui.base.BaseViewModel


class MainViewModel(application: Application) : BaseViewModel(application) {


    val isLogoutSuccess = MutableLiveData(false)

    fun logout() {
/*        return cmn0Service.logout()
            .switchThread()
            .autoProgressDialog()
            .catchApiError()
            .doOnSuccess {
                isLogoutSuccess.value = true
                SharedPrefModel.hasLogin = false
            }*/
    }

    //
    val onHomeTabClickEvent = LiveEvent<Event<Int>>()

    //Selecting States
    val selectedMode = MutableLiveData<Pair<Boolean, Int>>()
    val readLaterSelectedMode = MutableLiveData<Pair<Boolean, Int>>()
    val selectAllEvent = MutableLiveData<Boolean>(false)
    val readLaterSelectAllEvent = MutableLiveData<Boolean>(false)
    val readLaterEvent = MutableLiveData<Boolean>(false)
    val readLaterDeleteEvent = MutableLiveData<Boolean>(false)

    fun updateSelectedCount(count: Int = 1) {
        selectedMode.value = true to count
    }

    fun quiteSelectedMode() {
        selectedMode.value = false to 0
    }

    fun updateReadLaterSelectedCount(count: Int = 1) {
        readLaterSelectedMode.value = true to count
    }

    fun quiteReadLaterSelectedMode() {
        readLaterSelectedMode.value = false to 0
    }

    fun addReadLater() {
        readLaterEvent.value = true
    }

    fun deleteReadLater() {
        readLaterDeleteEvent.value = true
    }

    val bottomTabsAlpha = MutableLiveData(1F)

}