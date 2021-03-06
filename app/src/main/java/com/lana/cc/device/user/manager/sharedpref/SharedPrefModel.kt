package com.lana.cc.device.user.manager.sharedpref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonPref
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.util.Constants


object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME
    var hasLogin: Boolean by booleanPref(false)
    var userEmail: String by stringPref()
    var uid: Int by intPref()
    var password: String by stringPref()
    var rememberPassword:Boolean by booleanPref()
    var token: String by stringPref()
    var classficationMap: MutableMap<Int, Category> by gsonPref(hashMapOf())
    var userSettingMap: MutableMap<String, UserModel> by gsonPref(hashMapOf())

    private fun getUserModel(userIdS: String): UserModel =
        userSettingMap[userIdS] ?: UserModel().apply {
            userSettingMap[userIdS] = this
        }

    fun getUserModel(): UserModel = getUserModel(userEmail)

    fun setUserModel(modify: UserModel.() -> Unit) {
        val map = userSettingMap
        val user = map[userEmail] ?: UserModel()
        user.apply { modify.invoke(this) }
        map[userEmail] = user
        userSettingMap = map
    }

    fun setDefault() {
        setUserModel {
            //userSetting = null
        }
    }
}
