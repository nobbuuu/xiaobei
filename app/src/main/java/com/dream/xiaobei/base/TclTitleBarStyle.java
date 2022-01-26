package com.dream.xiaobei.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.TextView;

import com.dream.xiaobei.R;
import com.hjq.bar.style.CommonBarStyle;

/**
 * @author: Yzq
 * @date: 2021/6/12
 */
public class TclTitleBarStyle extends CommonBarStyle {

    private final int textColor = 0xFF2D3132;
    private final int backgroundColorNorm = 0xFFFFFFFF;
    private final int backgroundColorPress = 0x90FFFFFF;

    @Override
    public TextView createLeftView(Context context) {
        TextView leftView = super.createLeftView(context);
        leftView.setTextColor(textColor);
//        setViewBackground(leftView, new SelectorDrawable.Builder()
//                .setDefault(new ColorDrawable(backgroundColorNorm))
//                .build());
        leftView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        Drawable drawable = getRippleDrawable(context);
//        if (drawable != null) {
//            setViewBackground(leftView, drawable);
//        }

        return leftView;
    }

    @Override
    public TextView createTitleView(Context context) {
        TextView titleView = super.createTitleView(context);
        titleView.setTextColor(textColor);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return titleView;
    }

    @Override
    public TextView createRightView(Context context) {
        TextView rightView = super.createRightView(context);
        rightView.setTextColor(textColor);
//        setViewBackground(rightView, new SelectorDrawable.Builder()
//                .setDefault(new ColorDrawable(backgroundColorNorm))
//                .build());
        rightView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        Drawable drawable = getRippleDrawable(context);
//        if (drawable != null) {
//            setViewBackground(rightView, drawable);
//        }
        return rightView;
    }

    @Override
    public int createHorizontalPadding(Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
    }

    @Override
    public Drawable createBackIcon(Context context) {
        return getDrawableResources(context, R.mipmap.ic_back_black);
    }

    @Override
    public Drawable createBackgroundDrawable(Context context) {
        return new ColorDrawable(backgroundColorNorm);
    }

    /**
     * 获取水波纹的点击效果
     */
    public Drawable getRippleDrawable(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
            return getDrawableResources(context, typedValue.resourceId);
        }
        return null;
    }
}
