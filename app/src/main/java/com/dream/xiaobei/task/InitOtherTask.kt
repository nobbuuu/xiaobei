package com.dream.xiaobei.task

import com.blankj.utilcode.util.LogUtils
import com.dream.xiaobei.BuildConfig
import com.dream.xiaobei.base.TclTitleBarStyle
import com.hjq.bar.TitleBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tcl.launcher.task.Task

/**
 * @author : Yzq
 * time : 2020/11/10 13:55
 */
class InitOtherTask : Task() {
    override fun run() {
        LogUtils.getConfig().let {
            val debugMode = BuildConfig.DEBUG
            it.globalTag = "TsalesTag"
            it.setConsoleFilter(if (debugMode) LogUtils.V else LogUtils.W)
            it.isLog2FileSwitch = true
        }
        LiveEventBus.config().lifecycleObserverAlwaysActive(true).enableLogger(BuildConfig.DEBUG)
        initSmartRefresh()
        initTitleBar()
    }

    private fun initSmartRefresh() {
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
            //开启越界拖动
            layout.setEnableOverScrollDrag(true)
            layout.setEnableHeaderTranslationContent(true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(
                context
            )
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(
                context
            )
        }
    }


    private fun initTitleBar() {
        TitleBar.setDefaultStyle(TclTitleBarStyle())
    }


}