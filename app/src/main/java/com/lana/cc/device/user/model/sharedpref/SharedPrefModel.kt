package com.lana.cc.device.user.model.sharedpref

import com.chibatching.kotpref.KotprefModel
import com.lana.cc.device.user.util.Constants


object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME
    var accessToken: String by stringPref()
    var tokenInfo : String by stringPref()
  

}