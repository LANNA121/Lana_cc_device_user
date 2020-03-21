package com.lana.cc.device.user.ui.fragment.shop

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogAddressBinding
import com.lana.cc.device.user.databinding.DialogUserBinding
import com.lana.cc.device.user.databinding.FragmentShopBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.UserAddressInfo
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.ui.activity.MainActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.widget.DatePopView
import com.lana.cc.device.user.util.string2Date
import kotlinx.android.synthetic.main.activity_main.*

class ShopFragment : BaseFragment<FragmentShopBinding, ShopViewModel>(
    ShopViewModel::class.java, layoutRes = R.layout.fragment_shop
) {
    override fun initView() {

        binding.tvCoins.setOnClickListener {

        }

        binding.cardEarnCoin.setOnClickListener {
            (activity as MainActivity).bottomnavigation.selectedItemId = R.id.navigation_test
        }

        binding.tvCoinsHistory.setOnClickListener {
            findNavController().navigate(R.id.action_ShopFragment_to_CoinHistoryFragment)
        }



        binding.recShop.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ShopRecyclerAdapter(
                {
                    //商品单项点击事件
                },
                {
                    //商品单项兑换按钮点击事件
                    showAddressDialog(
                        UserAddressInfo(
                            SharedPrefModel.getUserModel().receiveName,
                            SharedPrefModel.getUserModel().receivePhone,
                            SharedPrefModel.getUserModel().receiveAddress
                        )
                    ) {
                        //存收货地址
                        SharedPrefModel.setUserModel {
                            receiveName = it.name
                            receivePhone = it.phone
                            receiveAddress = it.address
                        }
                        //Todo 调用兑换接口

                    }
                })
        }

        viewModel.goodsList.observeNonNull {
            (binding.recShop.adapter as ShopRecyclerAdapter).replaceData(it)
        }

    }

    override fun initData() {
        viewModel.getGoodsList()
    }

    //弹窗方法
    @SuppressLint("SetTextI18n")
    private fun showAddressDialog(
        userAddressInfo: UserAddressInfo, action: (
            UserAddressInfo
        ) -> Unit
    ) {
        //dataBinding 建议查看官方文档
        val dialogBinding =
            DataBindingUtil.inflate<DialogAddressBinding>(
                LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
                R.layout.dialog_address,//弹窗的layout文件
                null,//填null 无需多了解
                false//填false无需多了解
            )
        dialogBinding.name = userAddressInfo.name
        dialogBinding.phone = userAddressInfo.phone
        dialogBinding.address = userAddressInfo.address

        //安卓原生弹窗  设置信息界面
        AlertDialog.Builder(context!!).setView(
            dialogBinding.root
        ).setCancelable(true)
            .setPositiveButton("完成") { _, _ ->
                //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
                action.invoke(
                    UserAddressInfo(
                        dialogBinding.name,
                        dialogBinding.phone,
                        dialogBinding.address
                    )
                )
            }
            .create()
            .show()
    }


}