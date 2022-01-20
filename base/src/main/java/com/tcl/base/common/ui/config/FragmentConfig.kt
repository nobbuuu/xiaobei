package com.tcl.base.common.ui.config

import com.tcl.base.R
import com.tcl.base.weiget.multistatepage.state.StateConstant

/**
 * @author: Yzq
 * @date: 2021/6/9
 *  Fragment配置的类
 */
class FragmentConfig(
    //是否添加状态栏View
    var addStateView: Boolean = false,

    //状态栏背景颜色
    var stateViewColor: Int = R.color.white,

    //BaseLoadFragment 初始化加载的文字提示
    var loadingMsg: String = "加载中",

    //BaseLoadFragment 初始化状态
    var normState: Int = StateConstant.NORM_LOAD
)