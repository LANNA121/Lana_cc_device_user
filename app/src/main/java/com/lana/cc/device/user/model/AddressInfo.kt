package com.lana.cc.device.user.model

data class AddressInfo(
    // 地址id
    val id: Int?,
    // 收件人uid
    val uid: Long?,
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
    val street: String?,
    //
    val status: Int?
){
    var isChecked = false

}