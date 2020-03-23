package com.lana.cc.device.user.model.api.search

import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel

data class SearchKeyConclusion(
    val name: String,
    val sortId: Int
){
    var category: Category? = null
    var isCorrect:Boolean = false //答题界面的PagerAdapter保存状态使用
    val answerText:String? = SharedPrefModel.classficationMap[sortId]?.name
}