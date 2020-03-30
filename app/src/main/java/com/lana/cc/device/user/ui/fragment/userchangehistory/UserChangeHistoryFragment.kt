package com.lana.cc.device.user.ui.fragment.userchangehistory

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogTrackBinding
import com.lana.cc.device.user.databinding.FragmentUserChangeHistoryBinding
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.fragment.mine.ExchangeGoodsHistoryModel


class UserChangeHistoryFragment :
    BaseFragment<FragmentUserChangeHistoryBinding, UserChangeHistoryViewModel>(
        UserChangeHistoryViewModel::class.java, layoutRes = R.layout.fragment_user_change_history
    ) {

    override fun initView() {
        binding.btnBack.setOnClickListener {
            activity?.finish()
        }
        binding.recExchangeGoodsHistory.run {
            layoutManager = LinearLayoutManager(context)
            adapter = GoodsHistoryListAdapterForOss(
                onClick = {},
                onReceiveClick = {
                    viewModel.receiveBill(it.id ?: "")
                },
                onSendClick = {
                    showTrackDialog(it)
                }
            )
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchExchangeGoodsHistoryList()
        }

        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.exchangeGoodsHistoryList.observeNonNull {
            (binding.recExchangeGoodsHistory.adapter as GoodsHistoryListAdapterForOss).replaceData(
                it
            )
        }

    }

    private fun showTrackDialog(exchangeGoodsHistoryModel: ExchangeGoodsHistoryModel) {
        val dialogBing = DataBindingUtil.inflate<DialogTrackBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_track,
            null,
            false

        )
        showViewDialog(dialogBing.root) {
            viewModel.sendBill(
                exchangeGoodsHistoryModel.id ?: "",
                dialogBing.trackId ?: ""
            )
        }
    }

    override fun initData() {
        viewModel.fetchExchangeGoodsHistoryList()
    }


}
