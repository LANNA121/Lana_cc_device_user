package com.lana.cc.device.user.model.api.shop

data class ExchangeGoodsRequestModel(
    val goodsId: Int,
    val addressId: Int,
    val lanaId: String
)