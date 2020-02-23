package com.lana.cc.device.user.model.api.mine

data class UpdateUserModel(
    private val birthday: Long?,
    private val nikeName: String?,
    private val signature: String?,
    private val avatar: String?
)

