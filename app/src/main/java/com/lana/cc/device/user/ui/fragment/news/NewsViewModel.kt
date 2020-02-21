package com.lana.cc.device.user.ui.fragment.news

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.JuheService
import com.lana.cc.device.user.model.Photography
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.model.api.News
import io.reactivex.SingleTransformer
import com.lana.cc.device.user.ui.base.BaseViewModel
import jp.co.nikkei.t21.android.util.switchThread
import org.kodein.di.generic.instance


class NewsViewModel(application: Application) : BaseViewModel(application) {
    private val rubbishService by instance<RubbishService>()
    private val juheService by instance<JuheService>()
    val isRefreshing = MutableLiveData<Boolean>()
    val newsList = MutableLiveData<MutableList<News>>(mutableListOf())
    val photographyList = MutableLiveData<MutableList<Photography>>(mutableListOf())
    fun getNews() {
        juheService.getNews()
            .switchThread()
            .doOnSuccess {
                newsList.value = it.result.data
            }
            .bindLife()
    }
    fun getPhotography() {
        mockData()
/*		juheService.getPhotography()
			.switchThread()
			.doOnSuccess {
				photographyList.value = it
			}
			.compose(dealError())
			.compose(dealRefresh())
			.bindLife()*/
    }

    private fun mockData() {
        photographyList.value = mutableListOf(
            Photography(
                "",
                "",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1198494063,2664295875&fm=26&gp=0.jpg"
            ),
            Photography(
                "",
                "",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3242519757,1446733678&fm=26&gp=0.jpg"
            ),
            Photography(
                "",
                "",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=472761312,239862010&fm=26&gp=0.jpg"
            )
        )
    }



    fun search(str: String) {
        rubbishService.searchClassification(str)
            .switchThread()
            .catchApiError()
            .bindLife()
    }


    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs.doOnSubscribe { isRefreshing.postValue(true) }
                .doFinally { isRefreshing.postValue(false) }
        }
    }


}