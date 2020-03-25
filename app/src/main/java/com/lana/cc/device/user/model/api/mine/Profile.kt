package com.lana.cc.device.user.model.api.mine

import com.lana.cc.device.user.model.api.guide.register.ROLE_OSS
import com.lana.cc.device.user.model.api.guide.register.ROLE_USER

data class Profile(
    val uid: Int?,
    val role: String? = ROLE_USER,
    val userName: String?,
    val password: String?,
    val birthday: Long? = 0,
    val nikeName: String?,
    val gender: String?,
    val signature: String?,
    val avatar: String?,
    val background: String?,
    val status: Int?,
    val createTime: Long?,
    val updateTime: Long?,
    val coins: Int?
) {
    companion object {
        fun getDefault() = Profile(
            0,
            ROLE_USER,
            "",
            "",
            0,
            "",
            "",
            "",
            "",
            "",
            0,
            0,
            0,
            0
        )
    }
}


