package com.tcl.base.rxnetword

import android.app.Application
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import rxhttp.wrapper.ssl.HttpsUtils
import java.io.File
import java.util.concurrent.TimeUnit

object RxHttpManager {
    fun init(context: Application, interceptors: List<Interceptor>): OkHttpClient? {
       val timeout = 20L
        val file = File(context.externalCacheDir, "RxHttpCookie")
        var client: OkHttpClient? = null
        try {
            val sslParams = HttpsUtils.getSslSocketFactory()
            client = OkHttpClient.Builder() //                    .cookieJar(new CookieStore(file))
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .callTimeout(timeout, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .cookieJar(TclCookieStore(file)) //
                // .hostnameVerifier((hostname, session) -> true) //忽略host验证  //添加信任证书,用了默认的，忽略全部
                // .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
                // .addInterceptor(new TokenInterceptor())
                .apply {
                    interceptors.forEach {
                        addInterceptor(it)
                    }
                }
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return client
    }
}