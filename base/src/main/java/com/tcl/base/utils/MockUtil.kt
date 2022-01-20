package com.tcl.base.utils

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import java.io.InputStreamReader

/**
 * Author: tiaozi
 * Date : 2021/6/15
 * Drc:
 */
inline fun <reified T> getMockData(assetFileName: String, clazz: Class<T>): T {
    val input = Utils.getApp().resources.assets.open(assetFileName)
    return GsonUtils.fromJson(InputStreamReader(input), clazz)
}