package com.lana.cc.device.user.ui.fragment.mine

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemGoodsHistoryBinding
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter
import com.lana.cc.device.user.ui.utils.getImageFromServer

//商品历史列表的Adapter，继承自BaseRecyclerAdapter
class GoodsHistoryListAdapter(
    onClick: (ExchangeGoodsHistoryModel) -> Unit,
    val onFinishClick:(ExchangeGoodsHistoryModel)->Unit
) : BaseRecyclerAdapter<ExchangeGoodsHistoryModel, ItemGoodsHistoryBinding>(
    R.layout.item_goods_history,
    onClick
) {
    override fun bindData(binding: ItemGoodsHistoryBinding, position: Int) {
        val item = baseList[position]
        binding.goodsHistory = item
        binding.imgUrl = getImageFromServer(item.goodsDetail?.goodsUrl)
        binding.btnChangeStatus.setOnClickListener {
            onFinishClick(item)
        }
    }
}