package com.lana.cc.device.user.ui.fragment.coinhistory


import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemCoinHistoryBinding
import com.lana.cc.device.user.model.api.shop.AccountBook
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter

class CoinHistoryListAdapter(
    onCellClick: (AccountBook) -> Unit
) : BaseRecyclerAdapter<AccountBook, ItemCoinHistoryBinding>(
    R.layout.item_coin_history,
    onCellClick
) {
    override fun bindData(binding: ItemCoinHistoryBinding, position: Int) {
        binding.accountBook = baseList[position]
    }
}