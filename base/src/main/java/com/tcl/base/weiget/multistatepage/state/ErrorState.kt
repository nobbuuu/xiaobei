package com.tcl.base.weiget.multistatepage.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.tcl.base.R
import com.tcl.base.weiget.multistatepage.MultiState
import com.tcl.base.weiget.multistatepage.MultiStateContainer
import com.tcl.base.weiget.multistatepage.MultiStatePage

/**
 * @author : Yzq
 * time : 2020/11/17 9:34
 *
 */
class ErrorState : MultiState() {

    private lateinit var tvErrorMsg: TextView
    private lateinit var imgError: ImageView
    private lateinit var tvRetry: TextView

    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.mult_state_error, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvErrorMsg = view.findViewById(R.id.tv_error_msg)
        imgError = view.findViewById(R.id.img_error)
        tvRetry = view.findViewById(R.id.tv_retry)

        setErrorMsg(MultiStatePage.config.errorMsg)
        setErrorIcon(MultiStatePage.config.errorIcon)
    }

    override fun enableReload(): Boolean = true

    override fun bindRetryView(): View? {
        return tvRetry
    }

    fun setErrorMsg(errorMsg: String) {
        tvErrorMsg.text = errorMsg
    }

    fun setErrorIcon(@DrawableRes errorIcon: Int) {
        imgError.setImageResource(errorIcon)
    }
}