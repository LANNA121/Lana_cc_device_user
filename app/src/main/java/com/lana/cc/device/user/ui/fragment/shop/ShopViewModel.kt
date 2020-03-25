package com.lana.cc.device.user.ui.fragment.shop

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.ui.fragment.mine.upLoadImage
import com.lana.cc.device.user.util.MD5Utils
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
                goodsImageFile.postValue(null)
                fetchGoodsList()
            }
    }

    fun editGoods(
        goodsId: Int,
        goodsName: String,
        oldImageUrl:String,
        total: Int,
        price: Int,
        goodsDescription: String
    ) {
        fun editGoods(imagePath: String? = oldImageUrl) =
            goodsService.editGoods(
                EditGoodsRequestModel(
                    goodsId,
                    goodsName,
                    total,
                    price,
                    imagePath,
                    goodsDescription
                )
            )

        val single = if (goodsImageFile.value == null) {
            editGoods()
        } else {
            userService.upLoadImage(goodsImageFile.value)
                ?.flatMap {
                    editGoods(it.data?.imagePath)
                }
        }
        single?.doOnApiSuccess {
            goodsImageFile.postValue(null)
            fetchGoodsList()
        }
    }

    fun deleteGoods(
        goodsId: Int
    ) {
        goodsService.deleteGoods(goodsId)
            .doOnApiSuccess {
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


fun encodeByMd5(value: String?, slat: String?, uid: Int): String? {
    val passString = String.format("L%sA%sN%sA", value, slat, uid)
    return MD5Utils.calc(passString)
}