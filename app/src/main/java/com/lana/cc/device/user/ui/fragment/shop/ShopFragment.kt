package com.lana.cc.device.user.ui.fragment.shop

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogAddAddressBinding
import com.lana.cc.device.user.databinding.DialogAddressBinding
import com.lana.cc.device.user.databinding.DialogGoodsBinding
import com.lana.cc.device.user.databinding.FragmentShopBinding
import com.lana.cc.device.user.model.api.shop.ExchangeGoodsRequestModel
import com.lana.cc.device.user.model.api.shop.Goods
import com.lana.cc.device.user.ui.activity.ContentActivity
import com.lana.cc.device.user.ui.activity.MainActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.utils.getImageFromServer
import com.lana.cc.device.user.util.getProvinceModelList
import com.lana.cc.device.user.util.showSingleAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class ShopFragment : BaseFragment<FragmentShopBinding, ShopViewModel>(
    ShopViewModel::class.java, layoutRes = R.layout.fragment_shop
) {
    private lateinit var globalManageGoodsDialogBinding: DialogGoodsBinding
    private fun getManageGoodsDialogBinding(goods: Goods? = null): DialogGoodsBinding? {
        val dialogBinding = DataBindingUtil.inflate<DialogGoodsBinding>(
            LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
            R.layout.dialog_goods,//弹窗的layout文件
            null,//填null 无需多了解
            false//填false无需多了解
        )
        dialogBinding.goods = goods
        dialogBinding.imgUrl = getImageFromServer(goods?.goodsUrl)
        dialogBinding.goodsImage.setOnClickListener {
            showSingleAlbum()
        }
        globalManageGoodsDialogBinding = dialogBinding
        return dialogBinding
    }

    override fun initView() {

        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.goodsList.observeNonNull {
            (binding.recShop.adapter as ShopRecyclerAdapter).replaceData(it)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchGoodsList()
            viewModel.fetchCoins()
        }

        binding.cardAddGoods.setOnClickListener {
            showAddGoodsDialog {
                viewModel.addGoods(
                    globalManageGoodsDialogBinding.etName.text.toString(),
                    (if (globalManageGoodsDialogBinding.etTotal.text.toString().isEmpty()) "0" else globalManageGoodsDialogBinding.etTotal.text.toString()).toInt(),
                    (if (globalManageGoodsDialogBinding.etCoin.text.toString().isEmpty()) "0" else globalManageGoodsDialogBinding.etCoin.text.toString()).toInt(),
                    globalManageGoodsDialogBinding.etDes.text.toString()
                )
            }
        }

        binding.cardEarnCoin.setOnClickListener {
            (activity as MainActivity).bottomnavigation.selectedItemId = R.id.navigation_test
        }

        binding.tvCoinsHistory.setOnClickListener {
            findNavController().navigate(R.id.action_ShopFragment_to_CoinHistoryFragment)
        }

        binding.btnUserExchangeGoodsHistory.setOnClickListener {
            startActivity(
                ContentActivity.createIntent(
                    context!!,
                    ContentActivity.Destination.ExchangeGoodsHistory
                )
            )
        }

        binding.recShop.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ShopRecyclerAdapter(
                {
                    //商品单项点击事件
                }, { goods ->
                    //商品单项长按事件
                    showManageChooseDialog(
                        onEditClick = {
                            //点击编辑
                            showEditGoodsDialog(goods) {
                                viewModel.editGoods(
                                    goods.id,
                                    globalManageGoodsDialogBinding.etName.text.toString(),
                                    goods.goodsUrl ?: "",
                                    (if (globalManageGoodsDialogBinding.etTotal.text.toString().isEmpty()) "0" else globalManageGoodsDialogBinding.etTotal.text.toString()).toInt(),
                                    (if (globalManageGoodsDialogBinding.etCoin.text.toString().isEmpty()) "0" else globalManageGoodsDialogBinding.etCoin.text.toString()).toInt(),
                                    globalManageGoodsDialogBinding.etDes.text.toString()
                                )
                            }
                        },
                        onDeleteClick = {
                            //点击删除并确认（这里为下架，直接删除的话商品兑换会有矛盾）
                            viewModel.deleteGoods(goods.id)
                        })
                }, { goods ->
                    //商品单项兑换按钮点击事件
                    //地址弹窗
                    showAddressListDialog(goods.id)
                },
                viewModel.isOss.value ?: false
            )
        }
    }

    override fun initData() {
        viewModel.fetchGoodsList()
        viewModel.fetchCoins()
    }



    //地址列表弹窗
    private fun showAddressListDialog(
        goodsId: Int
    ) {
        //当前选中的addressId
        var chosenAddressId = 0
        //地址弹窗的binding对象
        val dialogBinding =
            DataBindingUtil.inflate<DialogAddressBinding>(
                LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
                R.layout.dialog_address,//弹窗的layout文件
                null,//填null 无需多了解
                false//填false无需多了解
            )

        //获取地址列表的方法
        fun fetchAddress() {
            viewModel.fetchAddressList { addressList ->
                chosenAddressId = if (addressList.isEmpty()) 0 else addressList[0].id ?: 0
                addressList.forEachIndexed { index, addressInfo ->
                    addressInfo.isChecked = (index == 0)
                }
                (dialogBinding.rvAddress.adapter as AddressListAdapter).replaceData(addressList)
            }
        }

        //删除地址弹窗
        fun showDeleteAddressDialog(addressid:Int){
            AlertDialog.Builder(context!!).setMessage("确认删除该地址吗？")
                .setPositiveButton("确认"){ _,_ ->
                    viewModel.deleteAddress(addressid){
                        fetchAddress()
                    }
                }
                .setNegativeButton("取消",null)
                .create().show()
        }

        //配置弹窗中的view
        dialogBinding.apply {
            btnAdd.setOnClickListener {
                showAddAddressDialog {
                    fetchAddress()
                }
            }
            rvAddress.run {
                adapter = AddressListAdapter(
                    { addressInfo,baseList -> //onClick
                        baseList.forEach { it.isChecked = false }
                        addressInfo.isChecked = true
                        adapter?.notifyDataSetChanged()
                        chosenAddressId = addressInfo.id ?: 0
                    },
                    {addressInfo -> //onLOngClick
                        showDeleteAddressDialog(addressInfo.id?:0)
                    }
                )
            }
        }
        showViewDialog(dialogBinding.root) {
            AlertDialog.Builder(context!!)
                .setTitle("确认兑换吗")
                .setPositiveButton("确认") { _, _ ->
                    //调用兑换商品接口
                    viewModel.exchangeGoods(
                        ExchangeGoodsRequestModel(
                            goodsId = goodsId,
                            addressId = chosenAddressId,
                            lanaId = UUID.randomUUID().toString()
                        )
                    ) {
                        AlertDialog.Builder(context!!)
                            .setTitle("兑换成功")
                            .setPositiveButton("确认", null)
                            .create().show()
                        viewModel.fetchCoins()
                    }
                }.setNegativeButton("否", null)
                .create().show()
        }
        //拉取地址列表
        fetchAddress()
    }



    //增加地址弹窗
    private fun showAddAddressDialog(onAdded: () -> Unit) {
        val provinceList = getProvinceModelList() ?: emptyList()
        var chosenProvincePosition = 0
        var chosenCityPosition = 0
        var chosenAreaPosition = 0
        //dataBinding 建议查看官方文档
        val dialogBinding =
            DataBindingUtil.inflate<DialogAddAddressBinding>(
                LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
                R.layout.dialog_add_address,//弹窗的layout文件
                null,//填null 无需多了解
                false//填false无需多了解
            ).apply {
                //省选择器
                spinnerProvince.run {
                    adapter = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        provinceList.map { it.name }
                    )
                    onItemSelectedListener = OnSpinnerItemSelected(
                        onItemSelectedAction = { provincePosition ->
                            chosenProvincePosition = provincePosition
                            //城市选择器
                            spinnerCity.run {
                                adapter =
                                    ArrayAdapter<String>(context!!,
                                        android.R.layout.simple_spinner_item,
                                        provinceList[provincePosition].city?.map { it.name }
                                            ?: emptyList()
                                    )
                                onItemSelectedListener = OnSpinnerItemSelected(
                                    onItemSelectedAction = { cityPosition ->
                                        chosenCityPosition = cityPosition
                                        //区县选择器
                                        spinnerDistrict.run {
                                            adapter = ArrayAdapter<String>(context!!,
                                                android.R.layout.simple_spinner_item,
                                                provinceList[provincePosition].city?.get(
                                                    cityPosition
                                                )?.area?.map { it.name }
                                                    ?: emptyList()
                                            )
                                            onItemSelectedListener = OnSpinnerItemSelected(
                                                onItemSelectedAction = { areaPosition ->
                                                    chosenAreaPosition = areaPosition
                                                })
                                        }
                                    })
                            }
                        })
                }
            }
        showViewDialog(dialogBinding.root) {
            //调用增加地址的接口
            viewModel.addNewAddress(
                dialogBinding.etName.text.toString(),
                dialogBinding.etPhone.text.toString(),
                provinceList[chosenProvincePosition].name,
                provinceList[chosenProvincePosition].city?.get(chosenCityPosition)?.name,
                provinceList[chosenProvincePosition].city?.get(chosenCityPosition)?.area?.get(
                    chosenAreaPosition
                )?.name,
                dialogBinding.etDetail.text.toString()
            ){
                onAdded()
            }
        }

    }

    //增加商品弹窗
    private fun showAddGoodsDialog(
        action: () -> Unit
    ) = showViewDialog(getManageGoodsDialogBinding()?.root, action)

    //编辑商品弹窗
    private fun showEditGoodsDialog(
        goods: Goods,
        action: () -> Unit
    ) = showViewDialog(getManageGoodsDialogBinding(goods)?.root, action)

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

class OnSpinnerItemSelected(
    val onItemSelectedAction: (Int) -> Unit,
    private val onNoSelectedAction: (() -> Unit)? = null
) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelectedAction(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        onNoSelectedAction?.invoke()
    }

}