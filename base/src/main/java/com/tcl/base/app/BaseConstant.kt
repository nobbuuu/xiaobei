package com.tcl.base.app

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
object BaseConstant {

    /**
     * 用来发送注销事件
     * 发送字段：Boolean（isRobLogin：是否需要显示被抢线登录提示）
     */
    const val EVENT_LOGOUT: String = "event_logout"
    const val EVENT_SALEMAN_EXCEPTION: String = "saleManException"
    const val EVENT_BINDWECHAT: String = "event_bindwechat"
    const val EVENT_ADD_SHIPPING_ADDRESS: String = "event_add_shipping_address"
    const val EVENT_ADD_BANKCARD: String = "event_add_bankcard"
    const val EVENT_GOTO_CATEGORY: String = "goto_category"

    const val LOGOUT_STATUS_CODE = 403
}