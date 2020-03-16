package com.lana.cc.device.user.model.api.search

data class SearchKeyConclusion(
    val name: String,
    val sortId: Int
){
    var category: Category? = null
}