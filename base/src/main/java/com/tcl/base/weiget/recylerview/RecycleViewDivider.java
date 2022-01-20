package com.tcl.base.weiget.recylerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description: RecycleView万能分割线
 * @author: icecold_laptop_2
 * @date: 2019/1/21
 */

public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    boolean includedLast = true;//最后一个item是否包含分割线 true 包含分割线 false 不包含分割线

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    public RecycleViewDivider(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public RecycleViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        if (mDivider == null) {
            mDividerHeight = 2;
            return;
        }
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childPosition = parent.getChildAdapterPosition(view);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            int lastCount;
            if (parent.getAdapter() == null) {
                lastCount = -1;
            }else {
                lastCount = parent.getAdapter().getItemCount() - 1;
            }
            //如果不是最后一条 正常赋值 如果是最后一条 赋值为0
            if (childPosition != lastCount) {
                outRect.set(0, 0, 0, mDividerHeight);
            }else {
                if (includedLast) {
                    outRect.set(0, 0, 0, mDividerHeight);
                }else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        } else {
            int lastCount;
            if (parent.getAdapter() == null) {
                lastCount = -1;
            }else {
                lastCount = parent.getAdapter().getItemCount() - 1;
            }
            //如果不是最后一条 正常赋值 如果是最后一条 赋值为0
            if (childPosition != lastCount) {
                outRect.set(0, 0, mDividerHeight, 0);
            }else {
                if (includedLast) {
                    outRect.set(0, 0, mDividerHeight, 0);
                }else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public boolean isIncludedLast() {
        return includedLast;
    }

    public void setIncludedLast(boolean includedLast) {
        this.includedLast = includedLast;
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int childSize;
        childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 绘制横向列表时的分隔线  这时分隔线是竖着的
     * l、r 变化； t、b 不变
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        int childSize;
        childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
