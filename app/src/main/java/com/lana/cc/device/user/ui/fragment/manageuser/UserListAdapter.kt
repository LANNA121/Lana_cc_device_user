package com.lana.cc.device.user.ui.fragment.manageuser

import com.lana.cc.device.user.BuildConfig
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.CellUserBinding
import com.lana.cc.device.user.model.api.guide.register.ROLE_OSS
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.ui.adapter.BaseRecyclerAdapter

class UserListAdapter(
    onCellClick: (Profile) -> Unit
) : BaseRecyclerAdapter<Profile, CellUserBinding>(
    R.layout.cell_user,
    onCellClick
) {
    override fun bindData(binding: CellUserBinding, position: Int) {
        binding.profile = baseList[position]
        binding.isOss = baseList[position].role == ROLE_OSS
        binding.img = "${BuildConfig.BASE_URL}/image/${baseList[position].avatar}"
    }
}