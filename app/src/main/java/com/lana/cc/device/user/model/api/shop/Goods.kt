package com.lana.cc.device.user.model.api.shop

data class Goods(
    val id: Int = 0,
    val goodsName: String? = null,
    val total: Int,
    val price: Int,
    val goodsUrl: String? = null,
    val goodsDescription: String? = null,
    val createBy: Int = 0,
    val createTime: Long = 0,
    val status: Int = 0
)

