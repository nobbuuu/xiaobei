package com.tcl.base.common.ui.config

import com.tcl.base.weiget.multistatepage.state.StateConstant

/**
 * Author: tiaozi
 * Date : 2021/6/8
 * Drc: 预留做Activity配置的类
 */

data class ActivityConfig(
    var isDoubleBack: Boolean = false,

    //BaseLoadActivity 初始化加载的文字提示
    var loadingMsg: String = "加载中",

    //BaseLoadActivity 初始化状态
    var normState :Int = StateConstant.NORM_LOAD
)
