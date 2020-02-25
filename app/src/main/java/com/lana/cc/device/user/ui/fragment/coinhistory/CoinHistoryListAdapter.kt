package com.lana.cc.device.user.ui.fragment.coinhistory


import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemCoinHistoryBinding
import com.lana.cc.device.user.model.api.coinhistory.CoinHistory
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter

class CoinHistoryListAdapter(
    onCellClick: (CoinHistory) -> Unit
) : BaseRecyclerAdapter<CoinHistory, ItemCoinHistoryBinding>(
    R.layout.item_coin_history,
    onCellClick
) {
    override fun bindData(binding: ItemCoinHistoryBinding, position: Int) {
        binding.coinHistory = baseList[position]
    }
}