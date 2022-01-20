package com.tcl.base.utils

import android.view.ViewGroup.MarginLayoutParams

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
object LayoutParamsUtils {
    /**
     * 使子view的topMargin和bottomMargin属性无效
     *
     * @param params
     */
    fun invalidTopAndBottomMargin(params: MarginLayoutParams?) {
        if (params != null) {
            params.topMargin = 0
            params.bottomMargin = 0
        }
    }
}