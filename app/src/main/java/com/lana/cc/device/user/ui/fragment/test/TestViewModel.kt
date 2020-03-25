package com.lana.cc.device.user.ui.fragment.test

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import com.lana.cc.device.user.model.api.test.ChangeRedeemRequestModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.util.MD5Utils
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.util.*

class TestViewModel(application: Application) : BaseViewModel(application) {

    val testList = MutableLiveData(emptyList<SearchKeyConclusion>())
    val title = MutableLiveData("")
    val currentPager = MutableLiveData(0)
    val hasClickedTip = MutableLiveData(false)
    private val rubbishService by instance<RubbishService>()
    private val userService by instance<UserService>()
    fun fetchTestList(doOnRefresh: () -> Unit) {
        rubbishService.fetchQuestion()
            .doOnApiSuccess {
                testList.postValue(it.data?.questionList)
                title.postValue("当前题目  1/${testList.value?.size}")
                doOnRefresh()
            }
    }

    fun changeRedeem(testPosition: Int) {
        val searchKeyConclusion = testList.value?.get(testPosition)
        val uuid = UUID.randomUUID().toString()
        val slat = "1${searchKeyConclusion?.name}"
        userService.changeRedeem(
            ChangeRedeemRequestModel(
                uuid,
                encodeByMd5(
                    uuid,
                    slat,
                    SharedPrefModel.uid
                ),
                slat,
                1
            )
        ).doOnApiSuccess {

        }

    }

    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread()
            .doOnSuccess {
                action?.invoke(it)
            }.catchApiError()
            .bindLife()
    }

    fun encodeByMd5(value: String?, slat: String?, uid: Int): String? {
        val passString = String.format("L%sA%sN%sA", value, slat, uid)
        return MD5Utils.calc(passString)
    }

}