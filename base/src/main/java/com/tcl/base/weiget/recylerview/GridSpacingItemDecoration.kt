package com.tcl.base.weiget.recylerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: tiaozi
 * Date : 2021/6/12
 * Drc:
 */
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val spacingV: Int = 0,
    private val includeEdge: Boolean
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) {
                if (spanCount == 1) {
                    if (position == 0) {
                        outRect.top = spacingV
                    }
                } else {
                    outRect.top = spacingV
                }
            }
            if (spanCount != 1) {
                outRect.bottom = spacingV
            }
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position < spanCount) {
                if (spanCount == 1) {
                    if (position == 0) {
                        outRect.top = spacingV
                    }
                } else {
                    outRect.top = spacingV
                }
            }
            if (spanCount != 1) {
                outRect.bottom = spacingV
            }
        }
    }
}