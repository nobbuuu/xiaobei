package com.tcl.base.common

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.rxLifeScope
import coil.network.HttpException
import com.blankj.utilcode.util.Utils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tcl.base.app.BaseConstant
import com.tcl.base.event.LogoutTipsBean
import com.tcl.base.event.Message
import com.tcl.base.event.SingleLiveEvent
import com.tcl.base.kt.ktToastShow
import com.tcl.base.kt.show
import com.tcl.base.rxnetword.exception.BusinessException
import com.tcl.base.rxnetword.exception.MultiDevicesException
import com.tcl.base.rxnetword.exception.NotSaleManException
import com.tcl.base.rxnetword.exception.TokenTimeOutException
import kotlinx.coroutines.CoroutineScope
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @auther : Yzq
 * time ： 2020/11/5 15:30
 */
open class BaseViewModel : AndroidViewModel(Utils.getApp()), LifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }


    /**
     *  所有网络请求都在 rxLifeScope 域中启动，当页面销毁时会自动
     *  https://github.com/liujingxing/RxLife-Coroutine
     *  block 需要协程执行的block
     *  errorBlock： 需要在自定义在异常情况执行的block
     *   showToast： 有异常信息的时候，是否弹Toast 显示msg，默认显示
     *   showDialog：是否在调接口的时候显示Loading弹窗，默认显示
     */
    fun rxLaunchUI(
        block: suspend CoroutineScope.() -> Unit,
        errorBlock: ((Throwable) -> Unit)? = null,
        showToast: Boolean = false,
        showDialog: Boolean = false,
        finalBlock: (() -> Unit)? = null
    ) {
        if (showDialog) {
            rxLifeScope.launch(
                block,
                { parseThrowable(it, errorBlock, showToast) },
                { defUI.showDialog.call() },
                onFinally = {
                    defUI.dismissDialog.call()
                    finalBlock?.invoke()
                }
            )
        } else {
            rxLifeScope.launch(block,
                { parseThrowable(it, errorBlock, showToast) },
                onFinally = { finalBlock?.invoke() })
        }
    }

    /**
     * 统一处理异常方法，有对登录过期做处理
     *
     */
    private fun parseThrowable(
        throwable: Throwable,
        errorBlock: ((Throwable) -> Unit)? = null,
        showToast: Boolean = true
    ) {
        throwable.printStackTrace()
        defUI.dismissDialog.call()
        when (throwable) {
            is NotSaleManException -> {
                LiveEventBus.get(BaseConstant.EVENT_SALEMAN_EXCEPTION).post(throwable.message)

                errorBlock?.invoke(throwable)
            }
            is TokenTimeOutException -> {
                blockLogoutEventAndDispatchEvent(throwable, errorBlock, showToast)

                errorBlock?.invoke(throwable)
            }
            is MultiDevicesException -> {
                LiveEventBus.get(BaseConstant.EVENT_LOGOUT)
                    .post(LogoutTipsBean(true, throwable.data))

                errorBlock?.invoke(throwable)
            }
            else -> {
                if (showToast) {
                    when (throwable) {
                        is ConnectException, is HttpException,
                        is SocketTimeoutException, is UnknownHostException -> "网络异常".ktToastShow()

                        is SSLException -> "证书出错".ktToastShow()

                        is BusinessException -> throwable.message.ktToastShow()

                        else -> {
                            throwable.show()
                            throwable.printStackTrace()
                        }
                    }
                }
                errorBlock?.invoke(throwable)
            }
        }
    }

    private fun doLogoutParser(exception: rxhttp.wrapper.exception.ParseException) {
        if (exception.errorCode == BaseConstant.LOGOUT_STATUS_CODE.toString()) {//code 是-500 的时候，表示token 过时，或者被占线
            LiveEventBus.get(BaseConstant.EVENT_LOGOUT).post(LogoutTipsBean(false, ""))
        }
        exception.show()
    }

    /**统一处理登出事件*/
    private fun blockLogoutEventAndDispatchEvent(
        throwable: TokenTimeOutException,
        errorBlock: ((Throwable) -> Unit)? = null,
        @Suppress("UNUSED_PARAMETER") showToast: Boolean = true
    ) {
        if (throwable.code == BaseConstant.LOGOUT_STATUS_CODE.toString()) {
            LiveEventBus.get(BaseConstant.EVENT_LOGOUT)
                .post(LogoutTipsBean(true, throwable.message ?: ""))
        } else {
            errorBlock?.invoke(throwable)
        }
    }

    /**
     * UI事件
     */
    inner class UIChange {
        val showDialog by lazy { SingleLiveEvent<String>() }
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
        val toastEvent by lazy { SingleLiveEvent<String>() }
        val msgEvent by lazy { SingleLiveEvent<Message>() }
    }

}







