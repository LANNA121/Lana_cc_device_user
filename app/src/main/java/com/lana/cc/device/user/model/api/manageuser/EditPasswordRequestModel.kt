package com.lana.cc.device.user.model.api.manageuser

data class EditPasswordRequestModel(
    val uid: Int,
    val password: String
)