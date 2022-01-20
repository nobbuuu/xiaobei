package com.tcl.base.weiget.wheelpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 月份选择器
 */
public class WheelMonthPicker extends WheelPicker implements IWheelMonthPicker {

    private int mSelectedMonth;

    public void updateMonths(boolean isCurrentYear) {
        int mSize = 12;
        if (isCurrentYear) {
            mSize = Calendar.getInstance().get(Calendar.MONTH) + 1;
        }
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= mSize; i++) {
            data.add(i);
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

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateMonths(true);
        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth - 1);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    @Override
    public void setSelectedMonth(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    @Override
    public int getCurrentMonth() {
        int currentItemPosition = getCurrentItemPosition() + 1;
        Log.i("WheelMonthPicker", "getCurrentMonth: " + currentItemPosition);
        return currentItemPosition;
    }
}