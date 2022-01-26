package com.dream.xiaobei.task

import android.app.Application
import android.webkit.WebView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.RomUtils
import com.dream.xiaobei.BuildConfig
import com.dream.xiaobei.app.UserManager
import com.kingswim.mock.HttpMockInterceptor
import com.tcl.base.rxnetword.RxHttpManager
import com.tcl.base.utils.MmkvUtil
import com.tcl.launcher.task.MainTask
import com.dream.xiaobei.other.security.EncryptTShopInterceptor
import okhttp3.Interceptor
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.param.RxHttp
import java.io.File

/**
 * @author : Yzq
 * time : 2020/11/10 14:01
 */
const val storeUuid = "thome"

class InitHttpClient : MainTask() {

    private val userAgent: String by lazy { WebView(mContext).settings.userAgentString }
    override fun run() {
        initRxHttp()
    }

    private fun initRxHttp() {
        when (mContext) {
            is Application -> {

                val interceptors =
                    mutableListOf<Interceptor>(
                        EncryptTShopInterceptor()
                    ).apply {
                        if (BuildConfig.DEBUG) {
                            //添加模拟数据拦截器
                            add(HttpMockInterceptor())
                        }
                    }
                RxHttp.init(RxHttpManager.init(mContext as Application, interceptors))
                val file = File(mContext.cacheDir, "netCache")
                RxHttpPlugins.setCache(file, 10 * 1024 * 1024, 60 * 1000)
                RxHttp.setDebug(BuildConfig.DEBUG, true)
                RxHttp.setOnParamAssembly { param ->
                    //根据不同请求添加不同参数，子线程执行，每次发送请求前都会被回调
                    //如果希望部分请求不回调这里，发请求前调用Param.setAssemblyEnabled(false)即可
                    param.addHeader("User-Agent", userAgent)
                        .addHeader("platform", "platform_tcl_shop")
                        .addHeader("storeUuid", storeUuid)//先用tclplus的数据，等后面通了，再用thome的数据
                        .addHeader("t-id", "TCL")
                        .addHeader("terminalType", "02")
                        .apply {
                            val accessToken = MmkvUtil.decryptGet(UserManager.getAccessTokenKey())
                            if (!accessToken.isNullOrEmpty()) {
                                addHeader("accessToken", accessToken)
                            }
                        }
                        .addHeader("appVersion", AppUtils.getAppVersionName())
                        .addHeader("source", "app")
                        .addHeader(
                            "pubChannel",
                            RomUtils.getRomInfo().name
                        )//例如：xiaomi，huawei，appstore
                        .addHeader("t-platform-type", "android")
                }
            }
            else -> {
                throw Exception("Context must be Application")
            }
        }
    }
}