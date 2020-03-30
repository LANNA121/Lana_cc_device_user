package com.lana.cc.device.user.ui.fragment.manageuser

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemGoodsHistoryForOssBinding
import com.lana.cc.device.user.databinding.ItemUserGoodsHistoryForOssBinding
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter
import com.lana.cc.device.user.ui.fragment.mine.ExchangeGoodsHistoryModel
import com.lana.cc.device.user.ui.utils.getImageFromServer

//商品历史列表的Adapter，继承自BaseRecyclerAdapter
class GoodsHistoryListAdapterForManageDialog: BaseRecyclerAdapter<ExchangeGoodsHistoryModel, ItemUserGoodsHistoryForOssBinding>(
    R.layout.item_user_goods_history_for_oss,
    {}
) {
    override fun bindData(binding: ItemUserGoodsHistoryForOssBinding, position: Int) {
        val item = baseList[position]
        binding.goodsHistory = item
    }
}