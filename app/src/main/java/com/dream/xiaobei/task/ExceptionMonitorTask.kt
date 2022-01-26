package com.dream.xiaobei.task

import com.blankj.utilcode.util.ActivityUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tcl.base.app.BaseConstant
import com.tcl.base.event.LogoutTipsBean
import com.tcl.base.kt.ktToastShow
import com.tcl.launcher.task.MainTask

/**
 * FileName: ExceptionMonitorTask
 * Author: mars
 * Date: 2021/8/3 11:55 PM
 * Description: 监控406,407
 */
class ExceptionMonitorTask : MainTask() {
    override fun run() {
        LiveEventBus.get(BaseConstant.EVENT_SALEMAN_EXCEPTION, String::class.java).observeForever {
            it?.ktToastShow()
        }

        LiveEventBus.get(BaseConstant.EVENT_LOGOUT, LogoutTipsBean::class.java).observeForever {
            if (it is LogoutTipsBean) {
            }
        }
    }
}