package com.lana.cc.device.user.ui.fragment.shop

import androidx.recyclerview.widget.GridLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentShopBinding
import com.lana.cc.device.user.ui.base.BaseFragment

class ShopFragment : BaseFragment<FragmentShopBinding, ShopViewModel>(
    ShopViewModel::class.java, layoutRes = R.layout.fragment_shop
) {
    override fun initView() {

        binding.tvCoins.setOnClickListener {

        }

        binding.tvCoinsHistory.setOnClickListener {

        }

        binding.recShop.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ShopRecyclerAdapter(
                {
                    //商品单项点击事件

                },
                {
                    //商品单项兑换按钮点击事件

                })
        }

        viewModel.goodsList.observeNonNull {
            (binding.recShop.adapter as ShopRecyclerAdapter).replaceData(it)
        }

    }

    override fun initData() {
        viewModel.getGoodsList()
    }

}