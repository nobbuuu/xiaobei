package com.tcl.base.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ruffian.library.widget.RRelativeLayout;
import com.tcl.base.R;

public class LeftRightLayout extends RRelativeLayout {

    private String mLeftTvText, mRightTvText;
    private boolean mShowDivider, mShowRightIcon;
    private TextView mTvLeft, mTvRight;

    public TextView getTvLeft() {
        return mTvLeft;
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    private View mViewDivider;

    public LeftRightLayout(Context context) {
        this(context, null);
    }

    public LeftRightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftRightLayout);

        mLeftTvText = typedArray.getString(R.styleable.LeftRightLayout_lr_left_text);
        mRightTvText = typedArray.getString(R.styleable.LeftRightLayout_lr_right_text);
        mShowDivider = typedArray.getBoolean(R.styleable.LeftRightLayout_lr_showDivider, false);
        mShowRightIcon = typedArray.getBoolean(R.styleable.LeftRightLayout_lr_showRightIcon, false);
        int leftTextColor = typedArray.getColor(R.styleable.LeftRightLayout_lr_left_text_color, ContextCompat.getColor(context, R.color.text_norm_gray));
        int rightTextColor = typedArray.getColor(R.styleable.LeftRightLayout_lr_right_text_color, ContextCompat.getColor(context, R.color.text_norm_black));

        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_left_right_item, this, true);
        mTvLeft = findViewById(R.id.tv_leftTag);
        mTvRight = findViewById(R.id.tv_rightTag);
        mViewDivider = findViewById(R.id.view_divider);
        findViewById(R.id.iv_rightIcon).setVisibility(mShowRightIcon ? VISIBLE : GONE);

        mTvLeft.setText(mLeftTvText);
        mTvLeft.setTextColor(leftTextColor);
        mTvRight.setText(mRightTvText);
        mTvRight.setTextColor(rightTextColor);
        mViewDivider.setVisibility(mShowDivider ? VISIBLE : INVISIBLE);
        getHelper().setBackgroundColorNormal(ContextCompat.getColor(context, R.color.color_while));
        getHelper().setRippleColor(ContextCompat.getColor(context, R.color.color_eee));
        getHelper().setUseRipple(true);
    }

    public void setRightTvText(String text) {
        mTvRight.setText(text);
    }

    public void setLeftIcon(Integer icon ){
        Drawable img = getResources().getDrawable(icon);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mTvLeft.setCompoundDrawables(img, null, null, null); //设置左图标
    }

    public void setLeftIcon(Drawable img){
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mTvLeft.setCompoundDrawables(img, null, null, null); //设置左图标
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        findViewById(R.id.area_content).setOnClickListener(clickListener);
    }

    public void setLeftTvText(String text) {
        mTvLeft.setText(text);
    }

    public void isShowDivider(boolean isShow) {
        mViewDivider.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

}
