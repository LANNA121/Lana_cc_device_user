package com.lana.cc.device.user.ui.fragment.news

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.NewsService
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.news.AddNewsRequestModel
import com.lana.cc.device.user.model.api.news.EditNewsRequestModel
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.fragment.mine.upLoadImage
import io.reactivex.Single
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Observable
import org.kodein.di.generic.instance
import java.io.File

class NewsViewModel(application: Application) : BaseViewModel(application) {

    private val newsService by instance<NewsService>()
    private val userService by instance<UserService>()
    private val rubbishService by instance<RubbishService>()
    val topList = MutableLiveData(emptyList<News>())
    val newsList = MutableLiveData(emptyList<News>())
    val isRefreshing = MutableLiveData(false)

    fun fetchClassification() {
        Observable.fromIterable(listOf(1, 2, 3, 4))
            .flatMap { num ->
                rubbishService.searchCategoryInfo(num).toObservable()
            }.toList()
            .doOnApiSuccess {
                val map = mutableMapOf<Int, Category>()
                it.forEach {
                    map[it.data?.id ?: 0] = it.data!!
                }
                SharedPrefModel.classficationMap = map
            }
    }

    fun fetchNews() {
        newsService.getNews()
            .doOnApiSuccess {
                newsList.postValue(it.data?.newsList)
                topList.postValue(it.data?.topNewsList)
            }
    }

    fun addNews(
        title: String,
        type: Int,
        content: String,
        imageFile: File?,
        needTop: Int
    ) {
        userService.upLoadImage(imageFile)
            ?.flatMap {
                newsService.addNews(
                    AddNewsRequestModel(
                        title,
                        type,
                        content,
                        it.data?.imagePath,
                        needTop
                    )
                )
            }

            ?.doOnApiSuccess {
                fetchNews()
            }
    }

    fun editNews(
        id:Long,
        originImageUrl:String,
        title: String,
        content: String,
        imageFile: File?,
        needTop: Int
    ) {
        fun edit(imagePath: String) = newsService.editNews(
            EditNewsRequestModel(
                id,
                title,
                content,
                imagePath,
                needTop
            )
        )

        val single = if (imageFile != null) {
            userService.upLoadImage(imageFile)
                ?.flatMap {
                    edit(it.data?.imagePath ?: "")
                }
        } else edit(originImageUrl)
        single?.doOnApiSuccess {
            fetchNews()
        }

    }

    fun deleteNews(newsId: Long) {
        newsService.deleteNews(newsId).doOnApiSuccess {
            fetchNews()
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