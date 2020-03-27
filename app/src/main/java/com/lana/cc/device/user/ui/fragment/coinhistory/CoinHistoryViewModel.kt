package com.lana.cc.device.user.ui.fragment.coinhistory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.shop.AccountBook
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance

class CoinHistoryViewModel(application: Application) : BaseViewModel(application) {

    private val goodsService by instance<GoodsService>()
    val coinHistoryList = MutableLiveData(emptyList<AccountBook>())
    val isRefreshing = MutableLiveData(false)

    fun fetchCoinHistoryList() {
        goodsService.fetchAccountBookHistoryList(SharedPrefModel.uid)
            .doOnApiSuccess {
                coinHistoryList.postValue(it.data?.accountBooks)
            }
    }

    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread(
        ).doOnSuccess {
            action?.invoke(it)
        }
            .dealRefresh()
            .catchApiError()
            .bindLife()
    }

    private fun <T> Single<T>.dealRefresh() =
        doOnSubscribe { isRefreshing.postValue(true) }
            .doFinally { isRefreshing.postValue(false) }

}