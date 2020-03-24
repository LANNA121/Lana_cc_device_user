package com.lana.cc.device.user.ui.fragment.manageuser

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.NewsService
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.ui.base.BaseViewModel
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Observable
import org.kodein.di.generic.instance

class ManageUserViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()

    val isRefreshing = MutableLiveData(false)
    val userList = MutableLiveData(emptyList<Profile>())


    fun fetchUsers() {
        userService.fetchUserList()
            .doOnApiSuccess { userList.postValue(it.data?.userProfileRspList) }
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