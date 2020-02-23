package com.lana.cc.device.user.model.api.guide.register

const val ROLE_USER = "USER"
const val ROLE_OSS = "OSS"

data class RegisterRequestModel(
    var userName: String?,
    var password: String?,
    var birthday: Long?,
    var nikeName: String?,
    var gender: String?,
    var signature: String?,
    var avatar: String?,
    var role: String? = ROLE_USER
)