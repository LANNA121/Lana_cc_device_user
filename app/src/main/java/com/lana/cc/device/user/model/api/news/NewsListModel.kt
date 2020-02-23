package com.lana.cc.device.user.model.api.news

import com.lana.cc.device.user.model.api.Profile

data class NewsListModel(
    val topNewsList:List<News>,
    val newsList:List<News>
)

data class News(
    val id:Int,
    val title:String,
    val newsUrl:String,
    val image:String,
    val top:Int,
    val createTime:Long,
    val userProfileRsp:Profile,
    val status:Int
    )