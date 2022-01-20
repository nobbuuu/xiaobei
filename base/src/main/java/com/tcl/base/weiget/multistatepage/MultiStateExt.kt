package com.tcl.base.weiget.multistatepage

import android.app.Activity
import android.view.View
import com.tcl.base.weiget.multistatepage.state.EmptyState
import com.tcl.base.weiget.multistatepage.state.ErrorState
import com.tcl.base.weiget.multistatepage.state.LoadingState
import com.tcl.base.weiget.multistatepage.state.SuccessState

/**
 * @author: Yzq
 * @date: 2021/6/11
 * MultiStateContainer 扩展方法
 */
/*

fun MultiStateContainer.ktShowError(msg:String ="网络错误") {
    show<EmptyState> {
        it.setEmptyMsg(msg)
    }
}

fun MultiStateContainer.showError(callBack: (ErrorState) -> Unit = {}) {
    show<ErrorState> {
        callBack.invoke(it)
    }
}

fun MultiStateContainer.ktShowEmpty(msg:String ="暂无数据") {
    show<EmptyState> {
        it.setEmptyMsg(msg)
    }
}

fun MultiStateContainer.showEmpty(callBack: (EmptyState) -> Unit = {}) {
    show<EmptyState> {
        callBack.invoke(it)
    }
}

fun MultiStateContainer.ktShowLoading(msg:String ="加载中...") {
    show<LoadingState> {
        it.setLoadingMsg(msg)
    }
}

fun MultiStateContainer.showLoading(callBack: (LoadingState) -> Unit = {}) {
    show<LoadingState> {
        callBack.invoke(it)
    }
}*/

fun MultiStateContainer.ktShowSuccess(callBack: (SuccessState) -> Unit = {}) {
    show<SuccessState> {
        callBack.invoke(it)
    }
}

fun View.bindMultiState(onRetryEventListener: OnRetryEventListener = OnRetryEventListener {  }) =
    MultiStatePage.bindMultiState(this, onRetryEventListener)

fun Activity.bindMultiState(onRetryEventListener: OnRetryEventListener = OnRetryEventListener {  }) =
    MultiStatePage.bindMultiState(this, onRetryEventListener)