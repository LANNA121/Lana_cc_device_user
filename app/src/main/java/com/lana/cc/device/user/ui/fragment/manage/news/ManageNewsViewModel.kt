package com.lana.cc.device.user.ui.fragment.manage.news

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.NewsService
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.ui.base.BaseViewModel
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Observable
import org.kodein.di.generic.instance

class ManageNewsViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()
    private val newsService by instance<NewsService>()
    private val rubbishService by instance<RubbishService>()
    val topList = MutableLiveData(emptyList<News>())
    val newsList = MutableLiveData(emptyList<News>())
    val isRefreshing = MutableLiveData(false)

    fun fetchClassfication() {
        Observable.fromIterable(listOf(1,2,3,4))
            .flatMap {num ->
                rubbishService.searchCategoryInfo(num).toObservable()
        }.toList()
            .doOnApiSuccess {
                val map = mutableMapOf<Int,Category>()
                it.forEach {
                    map.put(it.data?.id?:0,it.data!!)
                }
                SharedPrefModel.classficationMap = map
            }
    }

    fun getNews() {
        newsService.getNews()
            .doOnApiSuccess {
                newsList.postValue(it.data?.newsList)
                topList.postValue(it.data?.topNewsList)
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