package com.lana.cc.device.user.ui.fragment.news

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.ui.base.BaseViewModel
import io.reactivex.Single
import jp.co.nikkei.t21.android.util.switchThread
import org.kodein.di.generic.instance


class NewsViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()
    val topList = MutableLiveData(emptyList<News>())
    val newsList = MutableLiveData(emptyList<News>())
    val isRefreshing = MutableLiveData(false)

    fun getNews() {
        userService.getNews()
            .doOnApiSuccess {
                newsList.postValue(it.data.newsList)
                topList.postValue(it.data.topNewsList)
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