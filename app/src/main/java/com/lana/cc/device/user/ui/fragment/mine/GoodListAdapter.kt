package com.lana.cc.device.user.ui.fragment.mine

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemGoodsHistoryBinding
import com.lana.cc.device.user.model.Good
import jp.co.nikkei.t21.android.ui.adapter.BaseRecyclerAdapter

class GoodListAdapter(
    onClick: (Good) -> Unit
) : BaseRecyclerAdapter<Good, ItemGoodsHistoryBinding>(
    R.layout.item_goods_history,
    onClick
) {
    override fun bindData(binding: ItemGoodsHistoryBinding, position: Int) {
        binding.good = baseList[position]
    }
}