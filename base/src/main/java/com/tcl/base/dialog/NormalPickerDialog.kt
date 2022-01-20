package com.tcl.base.dialog

import android.content.Context
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.tcl.base.R
import com.tcl.base.weiget.wheelpicker.WheelPicker


class NormalPickerDialog(
    context: Context,
    private val cancelButtonText: String = StringUtils.getString(R.string.btn_cancel),
    private val confirmButtonText: String = StringUtils.getString(R.string.btn_done),
    private val title: String = "",
    val data: List<String>,
    val confirmAction: (position: Int) -> Unit,
    val defaultPosition: Int = 0
) : BaseSelectorDialog(context, true, true), WheelPicker.OnItemSelectedListener {

    private lateinit var tv_cancel: TextView
    private lateinit var tv_confirm: TextView
    private lateinit var tvTitle: TextView
    private lateinit var picker: WheelPicker
    private var position: Int = 0

    override fun getLayoutId() = R.layout.dialog_normal_picker_selector

    override fun initView() {

        tv_cancel = findViewById(R.id.tv_cancel)
        tv_confirm = findViewById(R.id.tv_confirm)
        picker = findViewById(R.id.picker)
        tvTitle = findViewById(R.id.tvTitle)



        picker.setOnItemSelectedListener(this)

        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            confirmAction(position)
            dismiss()
        }
    }

    override fun initData() {

        tvTitle.text = title
        tv_cancel.text = cancelButtonText
        tv_confirm.text = confirmButtonText

        picker.data = data

        when {
            defaultPosition < 0 -> {
                picker.selectedItemPosition = 0
            }
            defaultPosition >= data.size -> {
                picker.selectedItemPosition = data.size - 1
            }
            else -> {
                picker.selectedItemPosition = defaultPosition
            }
        }

        picker.selectedItemPosition = defaultPosition
        this.position = picker.currentItemPosition

    }

    override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
        this.position = position
    }
}