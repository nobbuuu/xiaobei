package com.tcl.base.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.tcl.base.R

abstract class BaseSelectorDialog(context: Context, private val cancelable: Boolean = false, private val canceledOnTouchOutside: Boolean = false,private val gravity:Int = Gravity.BOTTOM) : Dialog(context, R.style.th_base_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setCancelable(cancelable)
        setCanceledOnTouchOutside(canceledOnTouchOutside)
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            //setWindowAnimations(R.style.th_dialog_bottom_out_in)
            setGravity(gravity)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }

        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

}