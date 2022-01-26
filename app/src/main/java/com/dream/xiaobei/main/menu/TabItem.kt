package com.dream.xiaobei.main.menu

import androidx.annotation.StringRes

/**
 * Author: tiaozi
 * Date : 2021/6/21
 * Drc:
 */
data class TabItem(
    @TabId val position: Int,
    val id: Int,
    @StringRes val text: Int,
    val lottieFile: String,
    val unRead: Boolean = false,
    val unReadCount: Int = 0
)