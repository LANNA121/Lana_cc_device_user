package com.lana.cc.device.user.util

import com.lana.cc.device.user.R
import com.lana.cc.device.user.model.base.ApiException

object ApiUtil {

    private val apiErrorMsg = mapOf(
        "exception.system" to R.string.service_error_exception_system,
        "cmn.passwd.unable" to R.string.service_error_cmn_automatic_login_unable,
        "cmn.login.userover.compulsion" to R.string.service_error_cmn_login_userover_compulsion,
        "cmn.automatic.login.unable" to R.string.service_error_cmn_automatic_login_unable,
        "cmn.chargecode.admin.invalidity" to R.string.service_error_cmn_chargecode_admin_invalidity,
        "CRPF_0001" to R.string.service_error_CRPF_0001
    )

/*    fun parseErrorMsg(e: Throwable): Pair<String, Int> {
        return when (e) {
            is ApiException -> "" to parseApiErrorMsg(e).second
            is IOException -> "" to R.string.api_error_network_error
            else -> "" to R.string.error_dialog_msg_error
        }
    }

    private fun parseApiErrorMsg(e: ApiException): Pair<String, Int> {
        return "" to (e.messageStr ?: R.string.api_error_network_error)
    }*/

    fun isLoginUnable(e: Throwable) =
        e is ApiException && e.code.toString() == "cmn.automatic.login.unable"


}