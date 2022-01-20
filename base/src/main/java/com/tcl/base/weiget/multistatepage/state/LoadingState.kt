package com.tcl.base.weiget.multistatepage.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tcl.base.R
import com.tcl.base.weiget.multistatepage.MultiState
import com.tcl.base.weiget.multistatepage.MultiStateContainer
import com.tcl.base.weiget.multistatepage.MultiStatePage

/**
 * @author : Yzq
 * time : 2020/11/17 9:34
 */
class LoadingState : MultiState() {
    private lateinit var tvLoadingMsg: TextView
    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.mult_state_loading, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvLoadingMsg = view.findViewById(R.id.tv_loading_msg)
        setLoadingMsg(MultiStatePage.config.loadingMsg)
    }


    fun setLoadingMsg(loadingMsg: String) {
        tvLoadingMsg.text = loadingMsg
    }

    override fun enableReload(): Boolean {
        return true
    }
}