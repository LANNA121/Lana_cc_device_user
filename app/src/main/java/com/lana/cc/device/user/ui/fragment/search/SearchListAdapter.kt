package com.lana.cc.device.user.ui.fragment.search


import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ItemSearchBinding
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter


class SearchListAdapter(
    onClick: (SearchKeyConclusion) -> Unit
) : BaseRecyclerAdapter<SearchKeyConclusion, ItemSearchBinding>(
    R.layout.item_search,
    onClick
) {
    override fun bindData(binding: ItemSearchBinding, position: Int) {
        binding.searchConclusion = baseList[position]
    }
}
