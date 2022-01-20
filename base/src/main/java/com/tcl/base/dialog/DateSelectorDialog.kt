package com.tcl.base.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.tcl.base.R
import com.tcl.base.weiget.wheelpicker.WheelDayPicker
import com.tcl.base.weiget.wheelpicker.WheelMonthPicker
import com.tcl.base.weiget.wheelpicker.WheelPicker
import com.tcl.base.weiget.wheelpicker.WheelYearPicker
import java.util.*


class DateSelectorDialog(
    context: Context,
    private val cancelButtonText: String = StringUtils.getString(R.string.btn_cancel),
    private val confirmButtonText: String = StringUtils.getString(R.string.btn_done),
    private val pickerType: Int = PICKER_TYPE_Y_M,
    private val title: String = ""
) : BaseSelectorDialog(context, true, true), WheelPicker.OnItemSelectedListener {

    companion object {
        const val PICKER_TYPE_Y_M_D = 1 // 年-月-日
        const val PICKER_TYPE_M_D = 2 // 月-日
        const val PICKER_TYPE_Y_M = 3 // 年-月
    }

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    val allStr = "全部"
    private lateinit var yearPicker: WheelYearPicker
    private lateinit var monthPicker: WheelMonthPicker
    private lateinit var dayPicker: WheelDayPicker
    private lateinit var tv_cancel: TextView
    private lateinit var tv_confirm: TextView
    private lateinit var tv_yearLabel: TextView
    private lateinit var tv_monthLabel: TextView
    private lateinit var tv_dayLabel: TextView
    private lateinit var tvTitle: TextView
    private lateinit var line1: View

    private val currentMonth = Calendar.getInstance()[Calendar.MONTH] + 1
    private val currentYear = Calendar.getInstance()[Calendar.YEAR]
    private var mOnDateSelectedListener: OnDateSelectedListener? = null

    override fun getLayoutId() = R.layout.dialog_date_selector

    fun setDefault(birthDay: String) {
        if (!birthDay.isNullOrEmpty() && birthDay.contains("-")) {
            val ymd = birthDay.split("-")
            ymd[0]?.let {
                mYear = it.toInt()
                yearPicker?.selectedYear = it.toInt()
            }
            ymd[1]?.let {
                mMonth = it.toInt()
                monthPicker?.updateMonths(mYear == currentYear && mMonth == currentMonth)
                monthPicker?.selectedMonth = it.toInt()
            }

            ymd[2]?.let {
                mDay = it.toInt()
                dayPicker?.setYearAndMonth(mYear, mMonth)
                dayPicker?.selectedDay = it.toInt()
            }
        }
    }

    fun addAllStr() {
        yearPicker?.addAllStr()
    }

    override fun initView() {

        yearPicker = findViewById(R.id.yearPicker)
        monthPicker = findViewById(R.id.monthPicker)
        dayPicker = findViewById(R.id.dayPicker)
        tv_cancel = findViewById(R.id.tv_cancel)
        tv_confirm = findViewById(R.id.tv_confirm)
        tv_yearLabel = findViewById(R.id.tv_yearLabel)
        tv_monthLabel = findViewById(R.id.tv_monthLabel)
        tv_dayLabel = findViewById(R.id.tv_dayLabel)
        tvTitle = findViewById(R.id.tvTitle)
        line1 = findViewById(R.id.line1)
        tvTitle.text = title

        if (title.isEmpty()) {
            tvTitle.visibility = View.GONE
            line1.visibility = View.GONE
        }


        when (pickerType) {
            PICKER_TYPE_M_D -> {
                yearPicker.visibility = View.GONE
                tv_yearLabel.visibility = View.GONE
                monthPicker.setOnItemSelectedListener(this)
                dayPicker.setOnItemSelectedListener(this)
            }
            PICKER_TYPE_Y_M -> {
                dayPicker.visibility = View.GONE
                tv_dayLabel.visibility = View.GONE
                yearPicker.setOnItemSelectedListener(this)
                monthPicker.setOnItemSelectedListener(this)
            }
            else -> { //默认为年-月-日
                yearPicker.setOnItemSelectedListener(this)
                monthPicker.setOnItemSelectedListener(this)
                dayPicker.setOnItemSelectedListener(this)
            }
        }
        tv_cancel.text = cancelButtonText
        tv_confirm.text = confirmButtonText

        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            mOnDateSelectedListener?.onDateSelected(mYear, mMonth, mDay)
            dismiss()
        }
    }

    override fun initData() {
        when (pickerType) {
            PICKER_TYPE_M_D -> {
                mMonth = monthPicker.currentMonth
                mDay = dayPicker.currentDay
            }
            else -> { //默认为年-月-日
                mYear = yearPicker.currentYear
                mMonth = monthPicker.currentMonth
                mDay = dayPicker.currentDay
            }
        }

    }

    fun setEndYear(year: Int) {
        yearPicker?.yearEnd = year
    }

    override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
        if (picker?.id == R.id.yearPicker) {
            val yearStr = data as String
            if (yearStr == allStr) {
                monthPicker?.visibility = View.INVISIBLE
                tv_yearLabel?.visibility = View.GONE
                tv_monthLabel?.visibility = View.GONE
                mYear = -1
            } else {
                mYear = yearStr.toInt()
                monthPicker?.visibility = View.VISIBLE
                tv_yearLabel?.visibility = View.VISIBLE
                tv_monthLabel?.visibility = View.VISIBLE
                monthPicker.updateMonths(mYear == yearPicker.yearEnd)
                if (mYear == yearPicker.yearEnd && mMonth == currentMonth) {
                    dayPicker.updateLimit()
                } else {
                    dayPicker.updateDays()
                }
            }
        } else if (picker?.id == R.id.monthPicker) {
            mMonth = position + 1
            dayPicker?.setYearAndMonth(mYear, mMonth)
        }
        mDay = dayPicker.currentDay
    }

    interface OnDateSelectedListener {
        fun onDateSelected(year: Int, month: Int, day: Int)
    }

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        this.mOnDateSelectedListener = listener
    }

}