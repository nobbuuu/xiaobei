package com.tcl.base.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.ruffian.library.widget.RRelativeLayout;
import com.tcl.base.R;

public class BaseRelativeLayout extends RRelativeLayout {

    public BaseRelativeLayout(Context context) {
        this(context, null);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRelativeLayout);
        boolean aBoolean = typedArray.getBoolean(R.styleable.BaseRelativeLayout_isShowBg, true);
        if (aBoolean){
            getHelper().setBackgroundColorNormal(ContextCompat.getColor(context,R.color.white));
        }
        getHelper().setRippleColor(ContextCompat.getColor(context,R.color.color_eee));
        getHelper().setUseRipple(true);
    }

}
