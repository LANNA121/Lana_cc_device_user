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

class BannerViewHolder(
    val list: MutableList<News>
) : MZViewHolder<News> {
    lateinit var photographyBinding: CellPhotographyBinding
    override fun createView(context: Context): View {
        photographyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.cell_photography, null, false
        )
        return photographyBinding.root
    }

    override fun onBind(p0: Context?, p1: Int, data: News?) {
        // 数据绑定
        photographyBinding.news = data
        photographyBinding.imgUrl = getImageFromServer(data?.image ?: "")
    }
}
