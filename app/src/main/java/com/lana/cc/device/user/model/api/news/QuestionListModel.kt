package com.lana.cc.device.user.model.api.news

import com.lana.cc.device.user.model.api.search.SearchKeyConclusion

data class QuestionListModel(
    val questionList: List<SearchKeyConclusion>
)