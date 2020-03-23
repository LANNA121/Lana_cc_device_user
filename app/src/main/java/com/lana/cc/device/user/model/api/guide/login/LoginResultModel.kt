package com.lana.cc.device.user.model.api.guide.login

data class LoginResultModel(
    val uid: Int,
    val token: String,
    val role: String
)