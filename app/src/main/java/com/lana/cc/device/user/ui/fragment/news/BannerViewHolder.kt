package com.lana.cc.device.user.ui.fragment.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.lana.cc.device.user.model.Photography
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CellPhotographyBinding
import com.zhouwei.mzbanner.holder.MZViewHolder

class BannerViewHolder(
	val list: MutableList<Photography>
) : MZViewHolder<Photography> {
	
	lateinit var photographyBinding: CellPhotographyBinding
	
	override fun createView(context: Context): View {
		photographyBinding = DataBindingUtil.inflate(
			LayoutInflater.from(context),
			R.layout.cell_photography, null, false
		)
		
		return photographyBinding.root
	}
	
	override fun onBind(p0: Context?, p1: Int, data: Photography?) {
		// 数据绑定
		photographyBinding.photography = data
	}
}
