package com.dream.xiaobei.app

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.dream.xiaobei.bean.UserInfo
import com.dream.xiaobei.common.MmkvConstant
import com.dream.xiaobei.common.MmkvConstant.KEY_ACCESS_TOKEN
import com.dream.xiaobei.common.MmkvConstant.KEY_ACCOUNTID
import com.dream.xiaobei.common.MmkvConstant.KEY_REFRESH_TOKEN
import com.dream.xiaobei.common.MmkvConstant.KEY_VISIBLE_INVITE
import com.dream.xiaobei.common.MmkvConstant.KEY_VISIBLE_MYMONEY
import com.dream.xiaobei.common.MmkvConstant.KEY_VISIBLE_PARTNER
import com.dream.xiaobei.common.MmkvConstant.KEY_VISIBLE_PROFIT
import com.dream.xiaobei.common.MmkvConstant.KEY_VISIBLE_WALLET
import com.tcl.base.kt.isNotEmptyOrNullString
import com.tcl.base.utils.MmkvUtil
import rxhttp.wrapper.utils.GsonUtil

/**
 * desc   : 用户信息管理类
 * author : tanksu
 * date   : 2019-11-17
 */
object UserManager {
    var userInfoBean: UserInfo? = null
    private val mCustomerLiveData: MutableLiveData<UserInfo?> = MutableLiveData()

    init {
        val userInfo = MmkvUtil.decryptGet(MmkvConstant.KEY_USER_INFO)
        LogUtils.iTag("tanksu", "本地储存的用户信息--->${userInfo}")
        if (userInfo?.isNotEmpty() == true) {
            userInfoBean = GsonUtil.fromJson(userInfo, UserInfo::class.java)
            mCustomerLiveData.postValue( userInfoBean)
        } else {
            userInfoBean = null
            mCustomerLiveData.postValue(userInfoBean)
        }
    }

    /**是否登录APP了，取决于accesstoken有没有值*/
    fun isLogin(): Boolean {
        val access = MmkvUtil.decryptGet(getAccessTokenKey())
        val result = access?.isNotEmptyOrNullString() ?: false
        LogUtils.i("tanksu", "result:$result ,accessToken->$access")
        return result
    }

    /**更新用户信息*/
    fun updateUserInfo(userInfoStr: String) {
        if (!userInfoStr.isNullOrEmpty()) {
            MmkvUtil.encryptSave(MmkvConstant.KEY_USER_INFO, userInfoStr)
            userInfoBean = GsonUtil.fromJson(userInfoStr, UserInfo::class.java)
        }
    }

    /**获取用户信息*/
    fun getUserInfo(): String? {
        return MmkvUtil.decryptGet(MmkvConstant.KEY_USER_INFO)
    }

    /**获取token*/
    fun getAccessToken(): String {
        return MmkvUtil.decryptGet(getAccessTokenKey()) ?: ""
    }

    /**获取AccountId*/
    fun getAccountId(): String {
        return MmkvUtil.decryptGet(getAccountIdKey()) ?: ""
    }

    /**清空本地数据*/
    fun clearUserInfo() {
        MmkvUtil.removeKey(getAccessTokenKey())
        MmkvUtil.removeKey(MmkvConstant.KEY_USER_INFO)
        MmkvUtil.removeKey(getAccountIdKey())
        MmkvUtil.removeKey(KEY_VISIBLE_INVITE)
        MmkvUtil.removeKey(KEY_VISIBLE_MYMONEY)
        MmkvUtil.removeKey(KEY_VISIBLE_PARTNER)
        MmkvUtil.removeKey(KEY_VISIBLE_WALLET)
        MmkvUtil.removeKey(KEY_VISIBLE_PROFIT)
        userInfoBean = null
        mCustomerLiveData.value = userInfoBean
    }

    /**获取唯一的前缀key，可以用来区分多账号*/
    fun getUserUniquePreKey(): String {
        return "tzj_"
    }

    fun getAccessTokenKey() = "${getUserUniquePreKey()}_${KEY_ACCESS_TOKEN}"
    fun getRefreshTokenKey() = "${getUserUniquePreKey()}_${KEY_REFRESH_TOKEN}"
    fun getAccountIdKey() = "${getUserUniquePreKey()}_${KEY_ACCOUNTID}"

}