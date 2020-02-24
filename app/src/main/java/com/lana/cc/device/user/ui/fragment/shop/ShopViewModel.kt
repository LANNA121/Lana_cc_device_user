package com.lana.cc.device.user.ui.fragment.shop

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.base.BaseViewModel

class ShopViewModel(application: Application) : BaseViewModel(application) {
    val goodsList = MutableLiveData(listOf<Goods>())
    fun getGoodsList() {
        val goods = Goods(
            123213,
            "Dior",
            "https://www.dior.cn/couture/var/dior/storage/images/19298687/25-chi-CN/pcd-miss-dior-rose-n-roses-eau-de-toilette2_1440_1200.jpg",
            "Dior的口红",
            123213,
            123213,
            213
        )
        goodsList.value = listOf(
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods,
            goods
        )
    }
}