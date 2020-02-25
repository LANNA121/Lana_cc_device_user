package com.lana.cc.device.user.ui.fragment.coinhistory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.model.api.coinhistory.CoinHistory
import com.lana.cc.device.user.ui.base.BaseViewModel

class CoinHistoryViewModel(application: Application) : BaseViewModel(application) {

    val coinHistoryList = MutableLiveData(emptyList<CoinHistory>())

    fun getCoinHistoryList(){
        val item = CoinHistory(
            "+1",
            1582604681729
        )
        coinHistoryList.value = listOf(
            item,
            item,
            item,
            item,
            item,
            item,
            item,
            item,
            item
        )
    }

}