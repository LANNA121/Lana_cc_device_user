package com.lana.cc.device.user.ui.fragment.test

import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentTestBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.ui.base.BaseFragment


class TestFragment : BaseFragment<FragmentTestBinding, TestViewModel>(
    TestViewModel::class.java, layoutRes = R.layout.fragment_test
) {

    override fun initView() {

        //观察测试列表数据变化
        viewModel.testList.observeNonNull {
            setTitle(0)
            //设置指定的banner数据
            binding.banner.run {
                setCanLoop(false)
                setPages(it.toList()) {
                    TestCardBannerViewHolder(it.toMutableList()) { chosenSortId,answerSortId,testCardBinding ->
                        if(chosenSortId == answerSortId) {
                            testCardBinding.tickView.visibility = View.VISIBLE
                            testCardBinding.tvAnswer.visibility = View.VISIBLE
                            testCardBinding.tvAnswer.text = SharedPrefModel.classficationMap[answerSortId?:1]?.name
                            testCardBinding.tickView.isChecked = true
                            testCardBinding.root.isEnabled = false
                        }else Toast.makeText(context,"答错咯~",Toast.LENGTH_SHORT).show()
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
                        setTitle(position)
                        pause()
                    }
                })
                pause()
            }
        }

    }

    override fun initData() {
        viewModel.fetchTestList()
    }

    private fun setTitle(position: Int) {
        viewModel.title.value =
            "当前题目  ${position + 1}/${viewModel.testList.value?.size}"
    }

}