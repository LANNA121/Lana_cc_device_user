package com.lana.cc.device.user.model.api.news

import com.lana.cc.device.user.model.api.mine.Profile

data class NewsListModel(
    val topNewsList: List<News>,
    val newsList: List<News>
)

data class News(
    val id: Long,
    val title: String,
    val type: Int,
    val content: String,
    val image: String,
    val top: Int,
    val createTime: Long,
    val userProfileRsp: Profile,
    val status: Int
)