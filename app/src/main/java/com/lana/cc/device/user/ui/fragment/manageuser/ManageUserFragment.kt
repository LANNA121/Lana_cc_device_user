package com.lana.cc.device.user.ui.fragment.manageuser

import androidx.recyclerview.widget.GridLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentManageUserBinding
import com.lana.cc.device.user.ui.base.BaseFragment

class ManageUserFragment : BaseFragment<FragmentManageUserBinding, ManageUserViewModel>(
    ManageUserViewModel::class.java, R.layout.fragment_manage_user
) {

    override fun initView() {
        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.userList.observeNonNull {
            (binding.recUser.adapter as UserListAdapter).replaceData(it)
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchUsers()
        }
        binding.recUser.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = UserListAdapter {

            }
        }
    }

    override fun initData() {
        viewModel.fetchUsers()
    }

}



