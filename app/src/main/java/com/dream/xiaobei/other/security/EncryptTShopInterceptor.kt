package com.dream.xiaobei.other.security

import com.tcl.base.rxnetword.TclCookieStore
import com.dream.xiaobei.other.security.EncryptTShopHelper.CHANGE_BIND
import com.dream.xiaobei.other.security.EncryptTShopHelper.CHANGE_PASSWORD
import com.dream.xiaobei.other.security.EncryptTShopHelper.CHECK_ACCOUNT_STATUS
import com.dream.xiaobei.other.security.EncryptTShopHelper.CHECK_PHONE_SMS
import com.dream.xiaobei.other.security.EncryptTShopHelper.GET_PHONE_SMS
import com.dream.xiaobei.other.security.EncryptTShopHelper.JUDGE_PHONE_IS_BANDED
import com.dream.xiaobei.other.security.EncryptTShopHelper.LOGIN
import com.dream.xiaobei.other.security.EncryptTShopHelper.REGISTERED
import com.dream.xiaobei.other.security.EncryptTShopHelper.UPDATE_USER
import com.dream.xiaobei.other.security.EncryptTShopHelper.VERIFY_CODE
import com.dream.xiaobei.other.security.EncryptTShopHelper.WX_BIND_PHONE
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import rxhttp.wrapper.utils.LogUtil
import java.io.File
import java.net.URLEncoder


class EncryptTShopInterceptor : Interceptor {

    private val tShopEncryptPaths = listOf(LOGIN,REGISTERED,VERIFY_CODE,CHANGE_PASSWORD,CHECK_ACCOUNT_STATUS,CHANGE_BIND,WX_BIND_PHONE,CHECK_PHONE_SMS,JUDGE_PHONE_IS_BANDED,UPDATE_USER,GET_PHONE_SMS)


    private fun isTShopEncryptPath(url: String): Boolean {
        tShopEncryptPaths.forEach {
            if (url.contains(it, true)) {
                return true
            }
        }
        return false
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return responseEncrypt(chain)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chain.proceed(chain.request())
    }

    private fun responseEncrypt(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        val urlString = url.toString().let {
            if (it.contains("?")) {
                return@let it.split("?")[0]
            } else {
                return@let it
            }
        }

        if (EncryptTShopHelper.isEncryption && isTShopEncryptPath(urlString) ) {
            val queryParameterNames = url.queryParameterNames
            val urlBuilder = StringBuilder(urlString)
            //处理URL参数
            queryParameterNames.forEachIndexed { index, it ->
                if (index == 0) {
                    urlBuilder.append("?")
                }
                val value = EncryptTShopHelper.encryptBySection(data = url.queryParameter(it) ?: "")
                urlBuilder.append(it).append("=").append(URLEncoder.encode(value, "UTF-8"))

                if (index != queryParameterNames.size - 1) {
                    urlBuilder.append("&")
                }
            }

            val newBuilder = request.newBuilder()
            request.body?.apply {
                when (this) {
                    is FormBody -> {
                        val newFormBody = FormBody.Builder()
                        for (index in 0 until size) {
                            val name = name(index)
                            val value = EncryptTShopHelper.encryptBySection(data = value(index))
                            newFormBody.add(name, value)
                        }
                        val body = newFormBody.build()
                        newBuilder.method(request.method, body)
                    }
                    else -> {
                        val buf = Buffer()
                        writeTo(buf)
                        val readUtf8 = buf.readUtf8()
                        val contentType = contentType()
                        newBuilder.method(request.method, RequestBody.create(contentType,
                            EncryptTShopHelper.encryptJSONBySection(data = readUtf8)
                        ))
                    }
                }
            }
            val newRequest = newBuilder.url(urlBuilder.toString()).build()
            if(LogUtil.isDebug()){
                LogUtil.log(newRequest, TclCookieStore(File("")))
            }
            return chain.proceed(newRequest)
        } else {
            return chain.proceed(request)
        }
    }
}


