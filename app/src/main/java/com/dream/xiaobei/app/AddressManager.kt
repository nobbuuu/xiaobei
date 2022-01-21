package com.tcl.tclzjpro.app

import com.blankj.utilcode.util.LogUtils
import com.dream.xiaobei.app.UserManager
import com.tcl.base.utils.MmkvUtil
import com.tcl.tclzjpro.common.bean.AllAddressDataBean
import com.tcl.tclzjpro.common.constants.MmkvConstant
import rxhttp.wrapper.utils.GsonUtil

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
object AddressManager {
    private var allAddressDataBean: AllAddressDataBean? = null

    init {
        val addressStr = MmkvUtil.decryptGet(getAddressInfoKey()) ?: ""
        LogUtils.i("tanksu", "addressStr:${addressStr}")
        if (!addressStr.isNullOrEmpty()) {
            allAddressDataBean = GsonUtil.fromJson(addressStr, AllAddressDataBean::class.java)
        }
    }

    fun getUserAddressBean(): AllAddressDataBean {
        if (null == allAddressDataBean) allAddressDataBean = AllAddressDataBean()
        return allAddressDataBean!!
    }

    /**根据特殊的key，储存用户地址的bean*/
    fun saveUserAddress(addressStr: String) {
        if (!addressStr.isNullOrEmpty()) {
            MmkvUtil.encryptSave(getAddressInfoKey(), addressStr)
        }
    }

    /**根据特殊的key，获取用户地址的bean*/
    fun updateUserAddressBean(addressStr: String): AllAddressDataBean? {
        if (!addressStr.isNullOrEmpty()) {
            allAddressDataBean = GsonUtil.fromJson(addressStr, AllAddressDataBean::class.java)
            saveUserAddress(addressStr)
            return allAddressDataBean
        }
        return allAddressDataBean
    }

    fun getAddressInfoKey() = "${UserManager.getUserUniquePreKey()}_${MmkvConstant.KEY_ADDRESS_INFO}"
}