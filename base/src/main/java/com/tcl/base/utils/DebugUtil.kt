package com.tcl.base.utils

import android.os.SystemClock

/**
 *Author:tiaozi
 *DATE: 2021/7/23
 *DRC:调试用
 */

var startAppTime: Long = 0
fun runInTime(tag: String = "", function: () -> Any) {
    val start = SystemClock.currentThreadTimeMillis()
    function.invoke()
    val end = SystemClock.currentThreadTimeMillis()
    println("marsInControl $tag 总耗时：${end - start}")
}
