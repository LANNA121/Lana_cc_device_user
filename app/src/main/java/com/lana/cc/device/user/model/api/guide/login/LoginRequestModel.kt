package com.lana.cc.device.user.model.api.guide.login

import com.lana.cc.device.user.model.api.guide.register.ROLE_USER

data class LoginRequestModel(
    val userName:String,
    val password:String,
    val role:String = ROLE_USER
)