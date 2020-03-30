package com.lana.cc.device.user.ui.fragment.coinhistory

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentCoinHistoryBinding

import com.lana.cc.device.user.ui.base.BaseFragment


class CoinHistoryFragment : BaseFragment<FragmentCoinHistoryBinding, CoinHistoryViewModel>(
    CoinHistoryViewModel::class.java, layoutRes = R.layout.fragment_coin_history
) {

    override fun initView() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.recCoinHistory.run {
            layoutManager = LinearLayoutManager(context)
            adapter = CoinHistoryListAdapter{

            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchCoinHistoryList()
        }

        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.coinHistoryList.observeNonNull {
            (binding.recCoinHistory.adapter as CoinHistoryListAdapter).replaceData(it)
        }


    }

    override fun initData() {
        viewModel.fetchCoinHistoryList()
    }


}
