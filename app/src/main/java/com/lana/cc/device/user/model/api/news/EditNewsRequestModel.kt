package com.lana.cc.device.user.model.api.news

data class EditNewsRequestModel(
    val newsId:Long,
    val title: String,
    val newsUrl: String,
    val image: String?,
    val top: Int
)