package com.lana.cc.device.user.ui.fragment.manage.news

import com.lana.cc.device.user.BuildConfig
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CellNewsBinding
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter

class ManageNewsListAdapter(
    onCellClick: (News) -> Unit
) : BaseRecyclerAdapter<News, CellNewsBinding>(
    R.layout.cell_news,
    onCellClick
) {
    override fun bindData(binding: CellNewsBinding, position: Int) {
        binding.news = baseList[position]
        binding.img = "${BuildConfig.BASE_URL}/image/${baseList[position].image}"
    }
}