package com.tcl.base.weiget.wheelpicker;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 年份选择器
 */
public class WheelYearPicker extends WheelPicker implements IWheelYearPicker {

    private int mYearStart = 1900, mYearEnd = Calendar.getInstance().get(Calendar.YEAR);
    private int mSelectedYear;

    public WheelYearPicker(Context context) {
        this(context, null);
    }

    public WheelYearPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        updateYears();
        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        updateSelectedYear();
    }

    private void updateYears() {
        List<String> data = new ArrayList<>();
        for (int i = mYearStart; i <= mYearEnd; i++)
            data.add(i+"");
        super.setData(data);
    }

    public void addAllStr(){
        List<String> data = new ArrayList<>();
        for (int i = mYearStart; i <= mYearEnd; i++){
            data.add(i+"");
        }
        data.add("全部");
        super.setData(data);
    }
    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedYear - mYearStart);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelYearPicker");
    }

    @Override
    public void setYearFrame(int start, int end) {
        mYearStart = start;
        mYearEnd = end;
        mSelectedYear = getCurrentYear();
        updateYears();
        updateSelectedYear();
    }

    @Override
    public int getYearStart() {
        return mYearStart;
    }

    @Override
    public void setYearStart(int start) {
        mYearStart = start;
        mSelectedYear = getCurrentYear();
        updateYears();
        updateSelectedYear();
    }

    @Override
    public int getYearEnd() {
        return mYearEnd;
    }

    @Override
    public void setYearEnd(int end) {
        mYearEnd = end;
        updateYears();
    }

    @Override
    public int getSelectedYear() {
        return mSelectedYear;
    }

    @Override
    public void setSelectedYear(int year) {
        mSelectedYear = year;
        updateSelectedYear();
    }

    @Override
    public int getCurrentYear() {
        return Integer.parseInt(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}