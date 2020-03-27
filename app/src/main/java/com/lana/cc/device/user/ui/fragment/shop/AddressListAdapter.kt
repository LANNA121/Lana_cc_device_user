package com.lana.cc.device.user.ui.fragment.shop

import android.graphics.Color
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CellAddressInfoBinding
import com.lana.cc.device.user.model.AddressInfo
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter

class AddressListAdapter(
    onClick: (AddressInfo) -> Unit
) : BaseRecyclerAdapter<AddressInfo, CellAddressInfoBinding>(
    R.layout.cell_address_info,
    onClick
) {
    override fun bindData(binding: CellAddressInfoBinding, position: Int) {
        val item = baseList[position]
        binding.address = item
        if(item.isChecked){
            binding.root.setBackgroundColor(Color.parseColor("#F3F3F3"))
        }else{
            binding.root.setBackgroundColor(Color.WHITE)
        }
    }
}
