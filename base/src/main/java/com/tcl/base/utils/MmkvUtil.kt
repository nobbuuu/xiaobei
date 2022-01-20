package com.tcl.base.utils

import android.os.Parcelable
import com.tcl.base.utils.encipher.DesUtil
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * @author : Yzq
 * time : 2020/11/9 0:58
 * MmKu 代替sp 的存储方案
 */
object MmkvUtil {
    private var mmkv: MMKV? = null

    init {
        mmkv = MMKV.defaultMMKV()
    }

    fun encode(key: String, value: Any?) {
        when (value) {
            is String -> mmkv?.encode(key, value)
            is Float -> mmkv?.encode(key, value)
            is Boolean -> mmkv?.encode(key, value)
            is Int -> mmkv?.encode(key, value)
            is Long -> mmkv?.encode(key, value)
            is Double -> mmkv?.encode(key, value)
            is ByteArray -> mmkv?.encode(key, value)
            is Nothing -> return
        }
    }

    fun <T : Parcelable> encode(key: String, t: T?) {
        if (t == null) {
            return
        }
        mmkv?.encode(key, t)
    }

    fun encode(key: String, sets: Set<String>?) {
        if (sets == null) {
            return
        }
        mmkv?.encode(key, sets)
    }

    fun decodeInt(key: String): Int? {
        return mmkv?.decodeInt(key, 0)
    }

    fun decodeDouble(key: String): Double? {
        return mmkv?.decodeDouble(key, 0.00)
    }

    fun decodeLong(key: String): Long? {
        return mmkv?.decodeLong(key, 0L)
    }

    fun decodeBoolean(key: String): Boolean? {
        return mmkv?.decodeBool(key, false)
    }

    fun decodeBooleanOpen(key: String): Boolean? {
        return mmkv?.decodeBool(key, true)
    }

    fun decodeFloat(key: String): Float? {
        return mmkv?.decodeFloat(key, 0F)
    }

    fun decodeByteArray(key: String): ByteArray? {
        return mmkv?.decodeBytes(key)
    }

    fun decodeString(key: String?, default: String? = ""): String? {
        return mmkv?.decodeString(key, default)
    }

    fun decodeString(key: String): String? {
        return mmkv?.decodeString(key, "")
    }

    fun <T : Parcelable> decodeParcelable(key: String, tClass: Class<T>): T? {
        return mmkv?.decodeParcelable(key, tClass)
    }

    fun decodeStringSet(key: String): Set<String>? {
        return mmkv?.decodeStringSet(key, Collections.emptySet())
    }

    fun removeKey(key: String) {
        mmkv?.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv?.clearAll()
    }

    fun contains(key: String): Boolean {
        return mmkv?.contains(key) ?: false
    }

    /**加密保存*/
    fun encryptSave(key: String, content: String) {
        try {
            //加密后处再存放 added by tanksu on 20210413
            if (content.isNotEmpty())
                mmkv?.encode(key, DesUtil.encrypt(content))
            else
                mmkv?.encode(key, "")?.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**解密*/
    fun decryptGet(key: String, defaultValue: String? = ""): String? {
        var spValue = defaultValue
        try {
            val newValue = decodeString(key) ?: ""
            if (newValue.isNotEmpty())
                spValue = DesUtil.decrypt(newValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return spValue
    }

}