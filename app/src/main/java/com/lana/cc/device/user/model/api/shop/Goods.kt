package com.lana.cc.device.user.model.api.shop

data class Goods(
    val id: Long,//id
    val name: String,//名字
    val image: String,//名字
    val description: String,//描述
    val coin: Int,//价值
    val total: Int,//总数
    val currentCount: Int//剩余
)