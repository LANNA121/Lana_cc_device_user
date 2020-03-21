package com.lana.cc.device.user.manager.sharedpref

import com.lana.cc.device.user.model.api.guide.register.ROLE_USER


class UserModel {
    var receiveName: String? = null
    var receivePhone: String? = null
    var receiveAddress: String? = null
    var receiveSearchKey: String? = null
    var role: String? = ROLE_USER
}