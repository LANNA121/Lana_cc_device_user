package com.lana.cc.device.user.model.api.test

data class ChangeRedeemRequestModel(
    val lanaId: String?,
    val checkKey: String?,
    val source: String?,
    val point:Int = 0
)
