package com.tcl.base.rxnetword.parser

import android.text.TextUtils

class BaseResponse<T> {
    var code: String = "0"
    var message: String? = null
    var data: T? = null
    var status: Int = 0

    /**
     * 成功是0000
     */
    fun isSucceed(): Boolean {
        return TextUtils.equals(code, "0000")
    }
}