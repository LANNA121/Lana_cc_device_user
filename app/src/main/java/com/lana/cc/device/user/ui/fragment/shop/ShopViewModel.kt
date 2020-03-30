package com.lana.cc.device.user.ui.fragment.shop

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.GoodsService
import com.lana.cc.device.user.manager.api.UserService
import com.lana.cc.device.user.model.AddressInfo
import com.lana.cc.device.user.model.AddressRequestInfo
import com.lana.cc.device.user.model.api.shop.ExchangeGoodsRequestModel
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
    val coins = MutableLiveData(0)

    //拉取积分
    fun fetchCoins() {
        userService.fetchCoins()
            .doOnApiSuccess {
                coins.postValue(it.data)
            }
    }

    //拉取商品列表
    fun fetchGoodsList() {
        goodsService.fetchGoodsList()
            .doOnApiSuccess {
                goodsList.postValue(it.data)
            }
    }

    //增加商品
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

    //更改商品信息
    fun editGoods(
        goodsId: Int,
        goodsName: String,
        oldImageUrl: String,
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

    //兑换商品
    fun exchangeGoods(
        exchangeGoodsRequestModel: ExchangeGoodsRequestModel,
        action: () -> Unit
    ) {
        goodsService.exchangeGoods(exchangeGoodsRequestModel)
            .doOnApiSuccess {
                //兑换成功
                action()
            }
    }

    //拉取地址列表
    fun fetchAddressList(action: (List<AddressInfo>) -> Unit) {
        userService.fetchAddressList()
            .doOnApiSuccess {
                action(it.data?.addressList ?: emptyList())
            }
    }

    //增加新地址
    fun addNewAddress(
        name: String?,
        phone: String?,
        state: String?,
        city: String?,
        district: String?,
        street: String?,
        action: () -> Unit
    ) {
        userService.createNewAddress(
            AddressRequestInfo(
                name,
                phone,
                state,
                city,
                district,
                street
            )
        ).doOnApiSuccess { action() }
    }

    //删除地址
    fun deleteAddress(addressId: Int,onDeleted:()->Unit) {
        userService.deleteAddress(addressId)
            .doOnApiSuccess {
                onDeleted()
            }
    }

    //删除商品
    fun deleteGoods(
        goodsId: Int
    ) {
        goodsService.deleteGoods(goodsId)
            .doOnApiSuccess {
                fetchGoodsList()
            }
    }

    //更新刷新控件状态用的状态变量
    private fun <T> Single<T>.dealRefresh() =
        doOnSubscribe { isRefreshing.postValue(true) }
            .doFinally { isRefreshing.postValue(false) }

    //Single的扩展方法，网络请求成功后的操作
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