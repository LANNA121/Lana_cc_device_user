package com.lana.cc.device.user.ui.fragment.test

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.model.api.test.TestCard
import com.lana.cc.device.user.ui.base.BaseViewModel

class TestViewModel(application: Application) : BaseViewModel(application) {

    val testList = MutableLiveData(emptyList<TestCard>())
    val title = MutableLiveData("")
    fun getTestList() {

        val card = TestCard(
            0,
            "前男友是什么垃圾？",
            "有害垃圾"
        )

        testList.value = listOf(
            card,
            card,
            card,
            card,
            card,
            card,
            card,
            card,
            card
        )
        title.value = "当前题目  1/${testList.value?.size}"

    }
}