package com.lana.cc.device.user.ui.fragment.userchangehistory

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemGoodsHistoryBinding
import com.lana.cc.device.user.databinding.ItemGoodsHistoryForOssBinding
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter
import com.lana.cc.device.user.ui.fragment.mine.ExchangeGoodsHistoryModel
import com.lana.cc.device.user.ui.utils.getImageFromServer

//商品历史列表的Adapter，继承自BaseRecyclerAdapter
class GoodsHistoryListAdapterForOss(
    onClick: (ExchangeGoodsHistoryModel) -> Unit,
    val onReceiveClick: (ExchangeGoodsHistoryModel) -> Unit,
    val onSendClick: (ExchangeGoodsHistoryModel) -> Unit
) : BaseRecyclerAdapter<ExchangeGoodsHistoryModel, ItemGoodsHistoryForOssBinding>(
    R.layout.item_goods_history_for_oss,
    onClick
) {
    override fun bindData(binding: ItemGoodsHistoryForOssBinding, position: Int) {
        val item = baseList[position]
        binding.goodsHistory = item
        binding.btnChangeStateReceive.setOnClickListener { onReceiveClick(item) }
        binding.btnChangeStateSend.setOnClickListener { onSendClick(item) }
        binding.imgUrl = getImageFromServer(item.goodsDetail?.goodsUrl)
    }
}