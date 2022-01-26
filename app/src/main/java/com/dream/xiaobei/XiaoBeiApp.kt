package com.dream.xiaobei


import android.app.Activity
import android.app.Application
import android.os.SystemClock
import com.dream.xiaobei.task.CatchCrashTask
import com.dream.xiaobei.task.ExceptionMonitorTask
import com.dream.xiaobei.task.InitHttpClient
import com.dream.xiaobei.task.InitOtherTask
import com.tcl.base.BaseApplication
import com.tcl.base.utils.startAppTime
import com.tcl.launcher.TaskDispatcher
import com.tcl.tclzjpro.task.*
import com.tencent.mmkv.MMKV


/**
 * Author: tiaozi
 * Date : 2021/6/3
 * Drc:
 */
lateinit var mApplication: Application

@Suppress("UNREACHABLE_CODE")
class XiaoBeiApp : BaseApplication() {

    lateinit var browserJsActivityStack: MutableList<Activity>

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        browserJsActivityStack = mutableListOf()
        TaskDispatcher.init(this)
        //启动器进行异步初始化
        MMKV.initialize(this)

        TaskDispatcher.createInstance()
            .addTask(CatchCrashTask())
            .addTask(InitEnvironment())
            .addTask(InitHttpClient())
            .addTask(InitOtherTask())
            .addTask(ExceptionMonitorTask())
            .start()
        startAppTime = SystemClock.currentThreadTimeMillis()
    }
}

