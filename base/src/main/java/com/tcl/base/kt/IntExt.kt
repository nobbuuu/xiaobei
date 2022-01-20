package com.tcl.base.kt

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.text.DecimalFormat
import java.util.*

/**
 * Author: tiaozi
 * Date : 2021/6/26
 * Drc:
 */
/**倒计时*/
suspend fun Int.countDown(function: (Int) -> Unit) {
    var countDown = this@countDown
    flow {
         repeat(countDown) {
             delay(1000)
             countDown -= 1
             emit(countDown)
         }
     }.collect { countDown ->
         function.invoke(countDown)
     }
}

fun Double.keep2Decimal(): String {
    return DecimalFormat("#.00").format(this)
}