package com.lana.cc.device.user.ui.fragment.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CardTestBinding
import com.lana.cc.device.user.model.api.test.TestCard

import com.zhouwei.mzbanner.holder.MZViewHolder

//使用第三方库MZBanner 需要自定义MZViewHolder
class TestCardBannerViewHolder(
    val list: MutableList<TestCard>,
    private val onItemClick: (TestCard?) -> Unit
) : MZViewHolder<TestCard> {
    //也就是Banner中每一个 View（这是时我们的card_test生成的View）的 一个Holder 也就是控制这个View如何显示的类
    // 写一个ViewHolder继承自MZViewHolder 里面范型填上我们的
    lateinit var cardTestBinding: CardTestBinding //card_test的DataBinding对象

    override fun createView(context: Context): View {//创建视图
        //利用 DataBindingUtil工具类的inflate方法把我们的card_test创建出来
        cardTestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.card_test, null, false
        )
        //这个方法需要返回创建出来的View，然后在Fragment中Banner去使用这个Holder，单项的视图就是这个布局了
        return cardTestBinding.root
    }

    // 数据绑定
    override fun onBind(p0: Context?, p1: Int, data: TestCard?) {
        //将binding中我们定义的变量赋值
        cardTestBinding.testCard = data
    }
}
