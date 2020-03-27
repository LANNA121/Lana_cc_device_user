package com.lana.cc.device.user.ui.fragment.userchangehistory


import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.model.api.shop.ChangeBillStatusRequestModel
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.fragment.mine.ExchangeGoodsHistoryModel
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance

class UserChangeHistoryViewModel(application: Application) : BaseViewModel(application) {
    private val goodsService by instance<GoodsService>()
    val exchangeGoodsHistoryList = MutableLiveData(emptyList<ExchangeGoodsHistoryModel>())
    val isRefreshing = MutableLiveData(false)

    fun fetchCoinHistoryList() {
        goodsService.fetchAllUsersExchangeGoodsHistoryList()
            .doOnApiSuccess {
                exchangeGoodsHistoryList.postValue(it.data?.bills?.sortedBy { it.createTime })
            }
    }

    //接单
    fun receiveBill(billId: String) {
        goodsService.changeBillStatus(
            ChangeBillStatusRequestModel(
                billId,
                billStatus = 1
            )
        ).doOnApiSuccess {
            fetchCoinHistoryList()
        }
    }

    //发货
    fun sendBill(billId: String, trackId: String) {
        goodsService.changeBillStatus(
            ChangeBillStatusRequestModel(
                billId,
                trackId = trackId,
                billStatus = 2
            )
        ).doOnApiSuccess {
            fetchCoinHistoryList()
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