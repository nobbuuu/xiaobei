package com.dream.xiaobei.other.security

import com.tcl.base.utils.encipher.RSAUtil
import com.tcl.tclzjpro.app.Configs
import org.json.JSONObject

object EncryptTShopHelper {


    //登陆
    const val LOGIN = "rest/sysback/salesman/salesman/login"
    //注册
    const val REGISTERED = "sysback/salesman/registered/distributor"
    //修改密码验证验证码
    const val VERIFY_CODE = "/rest/usercenter/customer/forgetCheckVerifyCode"
    //修改密码
    const val CHANGE_PASSWORD = "/rest/usercenter/customer/update/password"
    //检查手机号或邮箱绑定状态
    const val CHECK_ACCOUNT_STATUS = "/rest/usercenter/customer/checkBindName"
    //手机换绑
    const val CHANGE_BIND = "/rest/usercenter/customer/changeBind"
    //微信绑定手机号
    const val WX_BIND_PHONE = "/rest/usercenter/customer/thirdParty/bindUserByAuthCode"
    //校验验证码
    const val CHECK_PHONE_SMS = "/rest/usercenter/customer/checkCaptcha"

    //判断手机号是否绑定过第三方信息
    const val JUDGE_PHONE_IS_BANDED = "/rest/usercenter/customer/judgePhoneIsBeenBanded"
    //更新用户信息
    const val UPDATE_USER = "/rest/usercenter/customer/updateUserMsg"
    //获取验证码
    const val GET_PHONE_SMS = "/rest/usercenter/customer/mobileCode"


    /**
     * 是否加密，默认加密
     */
    var isEncryption = true

    /**
     * 1024位密钥
     */
    @Volatile
    private var RSA_PUBLIC_KEY = Configs.getTShopPKey()

    fun encryptBySection(
        data: String
    ): String {
        return RSAUtil.publicEncrypt(RSA_PUBLIC_KEY, data)
    }

    fun encryptJSONBySection(
        data: String
    ): String {
        return try {
            val json = JSONObject(data)
            val encryptJson = JSONObject(data)
            json.keys().forEach { key ->
                val value = json.get(key).toString()
                encryptJson.put(key, encryptBySection(value))
            }
            encryptJson.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            data
        }
    }
}