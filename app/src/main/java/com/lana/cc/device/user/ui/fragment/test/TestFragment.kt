package com.lana.cc.device.user.ui.fragment.test

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentShopBinding
import com.lana.cc.device.user.databinding.FragmentTestBinding
import com.lana.cc.device.user.ui.base.BaseFragment

class TestFragment : BaseFragment<FragmentTestBinding, TestViewModel>(
    TestViewModel::class.java, layoutRes = R.layout.fragment_test
) {
    override fun initView() {}
    override fun initData() {

    }

}