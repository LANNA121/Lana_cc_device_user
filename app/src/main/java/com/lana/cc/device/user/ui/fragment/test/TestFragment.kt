package com.lana.cc.device.user.ui.fragment.test

import androidx.viewpager.widget.ViewPager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentShopBinding
import com.lana.cc.device.user.databinding.FragmentTestBinding
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.fragment.news.BannerViewHolder

class TestFragment : BaseFragment<FragmentTestBinding, TestViewModel>(
    TestViewModel::class.java, layoutRes = R.layout.fragment_test
) {
    override fun initView() {

        //观察测试列表数据变化
        viewModel.testList.observeNonNull {
            //设置指定的banner数据
            binding.banner.run {
                setCanLoop(false)
                setPages(it.toList()) {
                    TestCardBannerViewHolder(it.toMutableList()) { testCard ->

                    }
                }
                // 设置页面改变监听器
                addPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {

                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    //当页面改变时的回调
                    override fun onPageSelected(position: Int) {
                        //改变title
                        viewModel.title.value = "当前题目  ${position + 1}/${viewModel.testList.value?.size}"
                        pause()
                    }

                })
                pause()

            }
        }

    }

    override fun initData() {
        viewModel.getTestList()
    }

}