package com.tcl.base.kt

import java.lang.Exception
import java.math.BigDecimal

fun Any?.nullToEmpty(defVal: String = ""): String {
    return "${this ?: defVal}"
}

fun String?.deleteUnUseZero(defVal: String = ""): String {
    if (this == null) {
        return defVal
    }
    var result = this
    if (this.indexOf(".") > 0) {
        result = this.replace(Regex("0+?$"), "")
        result = result.replace(Regex("[.]$"), "")
    }
    return result
}

/**
 * 价格计算（避免丢失精度）
 */
fun String?.bigDecimalPrice(num: Int): String {
    try {
        if (this.isNullOrEmpty()) {
            return ""
        }
        return (BigDecimal(this).multiply(BigDecimal(num))).toString().deleteUnUseZero()
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}