package com.lana.cc.device.user.model.api.news

data class EditNewsRequestModel(
    val newsId:Long,
    val title: String,
    val content: String,
    val image: String?,
    val top: Int
)