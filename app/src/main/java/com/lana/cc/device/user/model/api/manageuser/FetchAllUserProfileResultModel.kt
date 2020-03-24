package com.lana.cc.device.user.model.api.manageuser

import com.lana.cc.device.user.model.api.mine.Profile

data class FetchAllUserProfileResultModel(
    val userProfileRspList:List<Profile>
)