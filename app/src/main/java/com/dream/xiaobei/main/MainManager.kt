package com.tcl.tclzjpro.main

/**
 * Author: tiaozi
 * Date : 2021/6/25
 * Drc:
 */
/*请求购物车角标*/
const val EVENT_REQUEST_UPDATE_CART_BADGE = "updateCartBadge"

/*刷新购物车角标*/
const val EVENT_NOTIFY_UPDATE_CART_BADGE = "notifyCartBadge"


const val EVENT_HOME_REFRESH = "EVENT_HOME_REFRESH"

/*检测新版本*/
const val EVENT_CHECK_VERSION = "checkNewVersion"

/**做新版本升级
 * 接收
 * @see #NewVersionBean
 * */
const val EVENT_DO_UPGRADE = "doUpgrade"

/**
 * 实名认证成功
 */
const val EVENT_DO_AUTHENTICATION_SUCCESS  = "EVENT_DO_AUTHENTICATION_SUCCESS"