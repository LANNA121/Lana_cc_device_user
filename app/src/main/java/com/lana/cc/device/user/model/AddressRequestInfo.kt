package com.lana.cc.device.user.model

data class AddressRequestInfo(
    // 收件人名称
    val name: String?,
    //收件人电话
    val phone: String?,
    //省
    val state: String?,
    //城市
    val city: String?,
    //行政街道
    val district: String?,
    //收件人门牌号
    val street: String?
)