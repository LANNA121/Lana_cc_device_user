package com.lana.cc.device.user.ui.fragment.shop


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogAddressBinding
import com.lana.cc.device.user.databinding.DialogGoodsBinding
import com.lana.cc.device.user.databinding.FragmentShopBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.UserAddressInfo
import com.lana.cc.device.user.ui.activity.MainActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.util.showSingleAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class ShopFragment : BaseFragment<FragmentShopBinding, ShopViewModel>(
    ShopViewModel::class.java, layoutRes = R.layout.fragment_shop
) {
    private lateinit var globalManageGoodsDialogBinding: DialogGoodsBinding

    private fun getManageGoodsDialogBinding() =
        DataBindingUtil.inflate<DialogGoodsBinding>(
            LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
            R.layout.dialog_goods,//弹窗的layout文件
            null,//填null 无需多了解
            false//填false无需多了解
        )

    override fun initView() {

        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.goodsList.observeNonNull {
            (binding.recShop.adapter as ShopRecyclerAdapter).replaceData(it)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchGoodsList()
        }

        binding.cardAddGoods.setOnClickListener {
            showAddGoodsDialog { goodsName, total, price, goodsDescription ->
                viewModel.addGoods(
                    goodsName,
                    total,
                    price,
                    goodsDescription
                )

            }
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

    }

    override fun initData() {
        viewModel.fetchGoodsList()
    }

    //兑换输入地址弹窗
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


    //增加商品弹窗
    private fun showAddGoodsDialog(
        action: (
            String, Int, Int, String
        ) -> Unit
    ) {
        //dataBinding 建议查看官方文档
        val dialogBinding = getManageGoodsDialogBinding()
        globalManageGoodsDialogBinding = dialogBinding

        dialogBinding.goodsImage.setOnClickListener {
            showSingleAlbum()
        }

        //安卓原生弹窗  设置信息界面
        AlertDialog.Builder(context!!).setView(
            dialogBinding.root
        ).setCancelable(true)
            .setPositiveButton("完成") { _, _ ->
                //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
                val name = dialogBinding.etName.text.toString()
                val total =
                    (if (dialogBinding.etTotal.text.toString().isEmpty()) "0" else dialogBinding.etTotal.text.toString()).toInt()
                val price =
                (if (dialogBinding.etCoin.text.toString().isEmpty()) "0" else dialogBinding.etCoin.text.toString()).toInt()
                val description = dialogBinding.etDes.text.toString()
                if (name.isNotEmpty()
                    && total > 0
                    && price > 0
                    && description.isNotEmpty()
                    && viewModel.goodsImageFile.value != null
                ) action.invoke(name, total, price, description)
            }
            .create()
            .show()
    }

    //选图后的回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images: List<LocalMedia>
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data)
                viewModel.goodsImageFile.value = File(images[0].path)
                globalManageGoodsDialogBinding.imgUrl = images[0].path
            }
        }
    }

}