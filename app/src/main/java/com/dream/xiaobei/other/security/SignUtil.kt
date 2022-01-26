package com.dream.xiaobei.other.security

import com.blankj.utilcode.util.EncryptUtils
import java.io.UnsupportedEncodingException
import java.util.*

object SignUtil {
    /**
     *
     *
     * Discription:[获取签名]
     *
     *
     * @return
     */
    fun getSign(param: Map<String, String>, key: String, input_charset: String): String {
        // 获取待签名字符串
        var text = createLinkString(param)
        text += key
        return EncryptUtils.encryptMD5ToString(getContentBytes(text, input_charset)).toLowerCase()
    }

    /**
     *
     *
     * Discription:[获取签名]
     *
     *
     * @return
     */
    fun getSign(param: Map<String, String>, key: String): String {
        return getSign(param, key, "UTF-8")
    }
    /**
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    private fun getContentBytes(content: String, charset: String): ByteArray {
        return if ("" == charset) {
            content.toByteArray()
        } else try {
            content.toByteArray(charset(charset))
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:$charset")
        }
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    fun createLinkString(params: Map<String, String>): String {
        val keys = ArrayList(params.keys)
        // 1. 参数名按照ASCII码表升序排序
        keys.sort()
        var prestr = ""
        for (i in keys.indices) {
            val key = keys[i]
            val value = params[key]
            // 拼接时，不包括最后一个&字符
            prestr = if (i == keys.size - 1) {
                "$prestr$key=$value"
            } else {
                "$prestr$key=$value&"
            }
        }
        return prestr
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(getSign(HashMap<String,String>().apply {
            put("appId", "37011623737219069")
            put("tenantId", "tcl")
            put("appSecret", "e545f8fbf9c659823e4a0b14383a6098a89cdb7da20122724efc0ce704e7a8b9")
            put("mobile","15271932314")
            put("bType","LOGIN")
            put("timestamp","1631953346009")
            put("nonce","391855")
        },"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxlE1rQPcPrzm6KKih2K1qYKuDcBjwGZc7uq8smakToYZk/zEnuU60zdG1j5X/nDZuVq5ArZKr/j9IDE/SaeW7mEo+M8+oZhQF0R1yQYwXBVyEtaG8GlLZacnvwn0lY/dtWxjbyusY3ivFQBvfjqo1w+3xMx5d72ozN21/368vwIDAQAB"))
    }
}