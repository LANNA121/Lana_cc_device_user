package com.lana.cc.device.user.ui.fragment.shop

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemGoodsBinding
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter


class ShopRecyclerAdapter(
    onGoodsClick: (Goods) -> Unit,
    val onExchangeClick: (Goods) -> Unit
) : BaseRecyclerAdapter<Goods, ItemGoodsBinding>(
    R.layout.item_goods,
    onGoodsClick
) {
    override fun bindData(binding: ItemGoodsBinding, position: Int) {
        val goods = baseList[position]
        binding.goods = goods
        binding.currentCount = "当前剩余 ${goods.currentCount}"
        binding.btnExchamge.setOnClickListener {
            onExchangeClick(goods)
        }
    }
}