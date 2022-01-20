package com.tcl.base.weiget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;  //位移间距
    private int mSpaceHalf;  //位移间距
    private int mColumnCount;  //位移间距
    private boolean hasHeader = false;

    public SpaceItemDecoration(int mColumnCount, int mSpace) {
        this.mSpace = mSpace;
        this.mSpaceHalf = (int) (mSpace * 0.5);
        this.mColumnCount = mColumnCount;
    }

    public SpaceItemDecoration(int mColumnCount, int mSpace, boolean hasHeader) {
        this.mSpace = mSpace;
        this.hasHeader = hasHeader;
        this.mSpaceHalf = (int) (mSpace * 0.5);
        this.mColumnCount = mColumnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (hasHeader) {
            headerSpaceItem(outRect, view, parent);
        } else {
            notHeaderSpaceItem(outRect, view, parent);
        }
    }

    /**
     * 没有头部
     *
     * @param outRect
     * @param view
     * @param parent
     */
    public void notHeaderSpaceItem(Rect outRect, View view, RecyclerView parent) {
        if (parent.getChildAdapterPosition(view) % mColumnCount == 0) {
            outRect.left = 0; //第一列左边贴边
            outRect.right = ConvertUtils.dp2px(mSpaceHalf);
        } else {
            outRect.left = ConvertUtils.dp2px(mSpaceHalf);//第二列移动一个位移间距
            outRect.right = 0;
        }
        if (parent.getChildAdapterPosition(view) >= mColumnCount) {
            outRect.top = ConvertUtils.dp2px(mSpace);
        } else {
            outRect.top = 0;
        }
    }

    /**
     * 没有头部
     *
     * @param outRect
     * @param view
     * @param parent
     */
    public void headerSpaceItem(Rect outRect, View view, RecyclerView parent) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = 0; //第一列左边贴边
            outRect.right = 0;
        } else {
            if (parent.getChildAdapterPosition(view) % mColumnCount == 1) {
                outRect.left = 0; //第一列左边贴边
                outRect.right = ConvertUtils.dp2px(mSpaceHalf);
            } else {
                outRect.left = ConvertUtils.dp2px(mSpaceHalf);//第二列移动一个位移间距
                outRect.right = 0;
            }
            outRect.top = ConvertUtils.dp2px(mSpace);
        }
    }


}
