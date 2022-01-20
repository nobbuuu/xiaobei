package com.tcl.base.weiget.recylerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *Author:tiaozi
 *DATE: 2021/7/23
 *DRC:
 */
class WaterFallItemDecoration(
    private val spacing: Int,
    private val spacingV: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = spacing / 2
        outRect.right = spacing / 2
        outRect.top = spacingV / 2
        outRect.bottom = spacingV / 2
    }
}