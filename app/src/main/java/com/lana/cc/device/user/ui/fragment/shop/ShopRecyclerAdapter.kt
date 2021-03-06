package com.lana.cc.device.user.ui.fragment.shop

import android.view.View
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemGoodsBinding
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter
import com.lana.cc.device.user.ui.utils.getImageFromServer


class ShopRecyclerAdapter(
    onGoodsClick: (Goods) -> Unit,
    val onGoodsLongClick: (Goods) -> Unit,
    val onExchangeClick: (Goods) -> Unit,
    val isOss:Boolean = false
) : BaseRecyclerAdapter<Goods, ItemGoodsBinding>(
    R.layout.item_goods,
    onGoodsClick
) {
    override fun bindData(binding: ItemGoodsBinding, position: Int) {
        val goods = baseList[position]
        binding.goods = goods.copy(goodsUrl = getImageFromServer(goods.goodsUrl))
        binding.btnExchamge.visibility = if(isOss)View.INVISIBLE else View.VISIBLE
        binding.btnExchamge.setOnClickListener {
            onExchangeClick(goods)
        }
        if(isOss){
            binding.root.setOnLongClickListener {
                onGoodsLongClick(goods)
                true
            }
        }
    }
}