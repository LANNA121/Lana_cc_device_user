package com.lana.cc.device.user.ui.fragment.shop

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.fragment.mine.upLoadImage
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.io.File

class ShopViewModel(application: Application) : BaseViewModel(application) {
    val goodsList = MutableLiveData(listOf<Goods>())
    val isRefreshing = MutableLiveData(false)
    private val goodsService by instance<GoodsService>()
    private val userService by instance<UserService>()
    val goodsImageFile = MutableLiveData<File>()

    fun fetchGoodsList() {
        goodsService.fetchGoodsList()
            .doOnApiSuccess {
                goodsList.postValue(it.data)
            }
    }

    fun addGoods(
        goodsName: String,
        total: Int,
        price: Int,
        goodsDescription: String
    ) {
        userService.upLoadImage(goodsImageFile.value)
            ?.flatMap {
                goodsService.addGoods(
                    AddGoodsRequestModel(
                        goodsName,
                        total,
                        price,
                        it.data?.imagePath,
                        goodsDescription
                    )
                )
            }?.doOnApiSuccess {
            fetchGoodsList()
        }

    }

    private fun <T> Single<T>.dealRefresh() =
        doOnSubscribe { isRefreshing.postValue(true) }
            .doFinally { isRefreshing.postValue(false) }


    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread(
        ).doOnSuccess {
            action?.invoke(it)
        }
            .dealRefresh()
            .catchApiError()
            .bindLife()
    }
}