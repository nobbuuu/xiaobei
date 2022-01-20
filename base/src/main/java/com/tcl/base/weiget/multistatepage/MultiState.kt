package com.tcl.base.weiget.multistatepage

import android.content.Context
import android.view.LayoutInflater
import android.view.View

/**
 * @author : Yzq
 * time : 2020/11/17 9:34
 * enableReload() 是否允许retry回调 false不允许
 * bindRetryView 绑定重试点击事件的view 默认为根view
 */
abstract class MultiState {

    /**
     * 创建stateView
     */
    abstract fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View

    /**
     * stateView创建完成
     */
    abstract fun onMultiStateViewCreate(view: View)

    /**
     * 是否允许重新加载 点击事件
     * 默认false 不允许
     */
    open fun enableReload(): Boolean = false

    /**
     * 绑定重试view
     * 默认null为整个state view
     */
    open fun bindRetryView(): View? = null

}