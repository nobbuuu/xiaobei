package com.dream.xiaobei.bean

import java.io.Serializable

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
/**用户bean*/

data class UserInfoResponse(val data:UserInfo):Serializable

data class UserInfo(
    val bindAddr: String,
    val bindMail: String,
    val bindPhone: String?,
    val birthday: String?,
    val city: String,
    val coupon: Int,
    val customerImgUrl: String,
    val customerName: String,
    val customerUuid: String,
    val fanliInfo: String,
    val hobby: String,
    val isBulkingAccount: Boolean,
    val jifen: String,
    val level: String,
    val nickname: String?,
    val privilege: String,
    val province: String,
    val realName: String,
    val region: String,
    val sex: String?,
    val staff: String?,
    val street: String,
    val ucUuid: String,
    val userType: String,
    val invitationCode: String?,
    val salesmanRankName: String,
    val avatar: String?,
    val identity: String?,
    val username: String?,
    val phone: String?,
    val idCard: String?,
    val salesmanRankLevel: String?
) :Serializable{

    /**是否是一级分销员（钻石合伙人）*/
    fun isLevelOneDistributor(): Boolean = "1" == salesmanRankLevel

    /**是否是二级分销员（普通合伙人）*/
    fun isLevelTwoDistributor(): Boolean = "0" == salesmanRankLevel

}

data class SalesManBean(
    val state: String?

) : Serializable {
    fun isPass(): Boolean {
        return state == "1" || state == "4" || state == "3"
    }

    /**根据不同的state获取string提示*/
    fun getStateStr(): String {
        when (state) {
            "-1" -> {
                return "手机号码未注册，请进行注册"
            }
            "0" -> {
                return "该账号正在审核中，请稍候"
            }
            "1", "4" -> {
                return "分销员审核通过"
            }
            "2" -> {
                return "该账号审核失败，请重新注册"
            }
            "3" -> {
                return "分销员已冻结"
            }
            "5" -> {
                return "账号被冻结"
            }
            else -> {
                return "账号状态未知"
            }
        }
    }
}