package com.tcl.base.weiget.wheelpicker;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期选择器
 */
public class WheelDayPicker extends WheelPicker implements IWheelDayPicker {
    private static final Map<Integer, List<Integer>> DAYS = new HashMap<>();

    private Calendar mCalendar;

    private int mYear, mMonth;
    private int mSelectedDay;

    public WheelDayPicker(Context context) {
        this(context, null);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        updateDays();
        mSelectedDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        updateSelectedDay();
    }

    public void updateDays() {
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);
        int days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<Integer> data = DAYS.get(days);
        if (null == data) {
            data = new ArrayList<>();
            for (int i = 1; i <= days; i++)
                data.add(i);
            DAYS.put(days, data);
        }

        super.setData(data);
    }

    @Override
    public String convertItemShowTxt(Object item) {
       if(item instanceof  Integer && (Integer) item < 10 ){
               return  "0" + item;
       }
        return  super.convertItemShowTxt(item);
    }

    private void updateSelectedDay() {
        setSelectedItemPosition(mSelectedDay - 1);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelDayPicker");
    }

    @Override
    public int getSelectedDay() {
        return mSelectedDay;
    }

    @Override
    public void setSelectedDay(int day) {
        mSelectedDay = day;
        updateSelectedDay();
    }

    @Override
    public int getCurrentDay() {
        return Integer.parseInt(String.valueOf(getData().get(getCurrentItemPosition())));
    }

    @Override
    public void setYearAndMonth(int year, int month) {
        mYear = year;
        mMonth = month - 1;
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        if (year == mYear && month == mMonth){
            updateLimit();
        }else {
            updateDays();
        }
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public void setYear(int year) {
        mYear = year;
        updateDays();
    }

    @Override
    public int getMonth() {
        return mMonth;
    }

    @Override
    public void setMonth(int month) {
        mMonth = month - 1;
        updateDays();
    }

    public void updateLimit() {
        int days = mCalendar.get(Calendar.DAY_OF_MONTH);
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            data.add(i);
        }
        super.setData(data);
    }
}