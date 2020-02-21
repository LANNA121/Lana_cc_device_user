package com.lana.cc.device.user.model.api.mine

import com.squareup.moshi.Json

data class UsrProfileResp(
	@Json(name = "usrProfileResp")
	val usrProfile: UsrProfile,
	val profileStatusResp: ProfileStatusResp
) {
	data class ProfileStatusResp(
		val completelyRegistrationFlag: String,
		val flagEmailVerify: String,
		val uinStatus: String,
		val uinType: String
	)
}

