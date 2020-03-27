package com.lana.cc.device.user.model.api.shop

import com.lana.cc.device.user.model.AddressInfo

data class FetchAddressListResultModel(
    val addressList: List<AddressInfo>
)