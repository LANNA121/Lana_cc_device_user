package com.lana.cc.device.user.ui.fragment.shop

data class AddGoodsRequestModel(
    val goodsName: String,
    val total: Int,
    val price: Int,
    val goodsUrl: String?,
    val goodsDescription: String
)