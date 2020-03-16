package com.lana.cc.device.user.ui.fragment.search.cameraSearch

import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CellThingBinding
import com.lana.cc.device.user.model.api.baiduidentify.Thing
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter


class ThingListAdapter(
	onCellClick: (Thing) -> Unit
) : BaseRecyclerAdapter<Thing, CellThingBinding>(
	R.layout.cell_thing,
	onCellClick
) {
	
	override fun bindData(binding: CellThingBinding, position: Int) {
		binding.thing = baseList[position]
	}
}
