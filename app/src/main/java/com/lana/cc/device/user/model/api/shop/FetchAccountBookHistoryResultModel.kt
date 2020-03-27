package com.lana.cc.device.user.model.api.shop

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchAccountBookHistoryResultModel(
    val accountBooks: List<AccountBook>
)

@JsonClass(generateAdapter = true)
data class AccountBook(
    /**
     * 账单ID
     */
    val lanaId: String,

    /**
     * 账单所有人UID
     */
    val uid: Int,

    /**
     * 账单类型
     */
    val type: String,

    /**
     * 数量
     */
    val count: Int,

    /**
     * 标记
     */
    val remark: String,

    /**
     * 创建时间
     */
    val createTime: Long,

    /**
     * 默认状态
     */
    val status: Int

){
    val typeOp = if(type == "achieve") "+" else ""

}