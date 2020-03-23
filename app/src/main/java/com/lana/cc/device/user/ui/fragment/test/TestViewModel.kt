package com.lana.cc.device.user.ui.fragment.test

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import com.lana.cc.device.user.model.api.test.TestCard
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance

class TestViewModel(application: Application) : BaseViewModel(application) {

    val testList = MutableLiveData(emptyList<SearchKeyConclusion>())
    val title = MutableLiveData("")
    private val rubbishService by instance<RubbishService>()
    fun fetchTestList() {
        rubbishService.fetchQuestion()
            .doOnApiSuccess {
                testList.postValue(it.data?.questionList)
                title.postValue("当前题目  1/${testList.value?.size}")
            }
    }

    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread()
            .doOnSuccess {
                action?.invoke(it)
            }.catchApiError()
            .bindLife()
    }

}