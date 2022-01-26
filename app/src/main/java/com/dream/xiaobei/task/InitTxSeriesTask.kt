package com.dream.xiaobei.task

import com.tcl.launcher.task.MainTask
import com.tencent.mmkv.MMKV

/**
 * Author: tiaozi
 * Date : 2021/6/3
 * Drc:
 */
class InitTxSeriesTask : MainTask() {

    override fun run() {
         //启动器进行异步初始化
        MMKV.initialize(mContext)

//        CrashReport.initCrashReport(
//            mContext,
//            Configs.getBuglyAppid(),
//            Configs.APP_PRODUCT_TYPE == Configs.curAppType
//        )
    }

}