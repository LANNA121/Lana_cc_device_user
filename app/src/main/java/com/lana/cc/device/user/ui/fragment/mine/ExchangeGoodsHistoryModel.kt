package com.lana.cc.device.user.ui.fragment.mine

import com.lana.cc.device.user.model.AddressInfo
import com.lana.cc.device.user.ui.utils.getImageFromServer

data class ExchangeGoodsHistoryModel(
    //兑换的账单ID
    val id: String?,
    // 账单所有人的ID
    val uid: Int?,
    // 账单所有人的信息
    val userInfo: UserInfo?,
    //账单兑换的商品
    val goodsDetail: GoodsDetail?,
    //账单的创建时间
    val createTime: Long?,
    //账单的物流编号
    val trackId: String?,
    //账单的处理状态
    val billStatus: Int?, //0 未处理 1 未发货 2 已发货 3 完成
    //处理人信息
    val operatorInfo: Operator?,
    //关联的地址
    val addressDetail: AddressInfo?,
    //默认状态
    val status: Int?
) {
    val state0Enabled = billStatus == 0
    val state1Enabled = billStatus == 1
    val state2Enabled = billStatus == 2
    val state3Enabled = billStatus == 3

    val billStateString = when (billStatus) {
        0 -> "未处理"
        1 -> "未发货"
        2 -> "已发货"
        3 -> "已完成"
        else -> "兑换状态获取失败"
    }

    data class GoodsDetail(
        val id: Int?,
        val goodsName: String?,
        val price: Int?,
        val goodsUrl: String?,
        val goodsDescription: String?

    )

    data class Operator(
        val id: Int?,
        val nikeName: String?,
        val avatar: String?
    )

    data class UserInfo(
        val id: Int?,
        val nikeName: String?,
        val avatar: String?,
        val userName: String?
    ) {
        val avatarUrl = getImageFromServer(avatar)
    }

}