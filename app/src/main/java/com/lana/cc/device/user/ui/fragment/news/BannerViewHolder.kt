package com.lana.cc.device.user.ui.fragment.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CellPhotographyBinding
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.ui.utils.getImageFromServer
import com.zhouwei.mzbanner.holder.MZViewHolder

//使用第三方库MZBanner 需要自定义MZViewHolder
class BannerViewHolder(
    val list: MutableList<News>,
    private val onItemClick: (News?) -> Unit,
    private val onItemLongClick: (News?) -> Unit
) : MZViewHolder<News> {
    //也就是Banner中每一个 View（这是时我们的cell_photography生成的View）的 一个Holder 也就是控制这个View如何显示的类
    // 写一个ViewHolder继承自MZViewHolder 里面范型填上我们的
    private lateinit var photographyBinding: CellPhotographyBinding //cell_photography的DataBinding对象

    override fun createView(context: Context): View {//创建视图
        //利用 DataBindingUtil工具类的inflate方法把我们的cell_photography创建出来
        photographyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.cell_photography, null, false
        )
        //这个方法需要返回创建出来的View，然后zaiFragment中Banner去使用这个Holder，单项的视图就是这个布局了
        return photographyBinding.root
    }

    // 数据绑定
    override fun onBind(p0: Context?, p1: Int, data: News?) {
        //将binding中我们定义的变量赋值
        photographyBinding.news = data
        photographyBinding.imgUrl = getImageFromServer(data?.image ?: "")
        photographyBinding.imgGrid.setOnClickListener {
            //设置图片点击后触发的事件为我们创建BannerViewHolder时传进来的action（也就是onItemClick）
            onItemClick.invoke(data)
        }
        photographyBinding.imgGrid.setOnLongClickListener {
            //设置图片点击后触发的事件为我们创建BannerViewHolder时传进来的action（也就是onItemClick）
            onItemLongClick.invoke(data)
            true
        }

    }
}
