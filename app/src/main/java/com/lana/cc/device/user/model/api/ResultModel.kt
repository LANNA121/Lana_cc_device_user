package com.lana.cc.device.user.model.api


data class ResultModel<T>(
    val meta: Meta,
    val data: T?
){
    data class Meta(
        val code: Int,
        val msg: String
    )
}


