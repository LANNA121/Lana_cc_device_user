package com.lana.cc.device.user.model.api.shop

data class ChangeBillStatusRequestModel(
    val billId: String,
    val trackId: String? = null,
    val billStatus: Int
)