@file:Suppress("SameParameterValue")

package com.tcl.base.weiget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.tcl.base.R
import kotlin.math.max
import kotlin.math.min

/**
 * desc   :
 * author : lfw
 * date   : 2021/3/4
 */
class NumberProgressBarEdition @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * The progress area bar color.
     */
    private var mReachedBarColor: Int


    /**
     * The progress text color.
     */
    var textColor: Int
        private set

    /**
     * The height of the reached area.
     */
    private var reachedBarHeight: Float

    /**
     * The height of the unreached area.
     */
    private var unreachedBarHeight: Float

    /**
     * The suffix of the number.
     */
    private var mSuffix = "%"

    /**
     * The prefix.
     */
    private var mPrefix = ""

    private var mCurIndicatorVisible: Boolean = true
    private var mUnReachableBarVisible: Boolean = false
    private var mTextVisible: Boolean = false


    private val defaultTextColor = Color.rgb(66, 145, 241)
    private val defaultReachedColor = Color.rgb(66, 145, 241)
    private val defaultUnreachedColor = Color.rgb(204, 204, 204)
    private val defaultProgressTextOffset: Float
    private val defaultTextSize: Float
    private val defaultReachedBarHeight: Float
    private val defaultUnreachedBarHeight: Float

    /**
     * The width of the text that to be drawn.
     */
    private var mDrawTextWidth = 0f

    /**
     * The drawn text start.
     */
    private var mDrawTextStart = 0f

    /**
     * The text that to be drawn in onDraw().
     */
    private var mCurrentDrawText: String = ""

    /**
     * The Paint of the reached area.
     */
    private lateinit var mReachedBarPaint: Paint

    /**
     * The Paint of the unreached area.
     */
    private lateinit var mUnreachedBarPaint: Paint

    /**
     * The Paint of the progress text.
     */
    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * Reached bar area rect.
     */
    private val mReachedRectF = RectF(0f, 0f, 0f, 0f)

    /**
     * The progress text offset.
     */
    private val mOffset: Float

    /**
     * Listener
     */
    private var mListener: OnProgressBarListener? = null

    /**
     * Get progress text size.
     *
     * @return progress text size.
     */
    private var progressTextSize: Float = 10f
        set(textSize) {
            field = textSize
            mTextPaint.textSize = field
            invalidate()
        }
    private var mUnreachedBarColor: Int = 0
        set(barColor) {
            field = barColor
            invalidate()
        }
    private var reachedBarColor: Int
        get() = mReachedBarColor
        set(progressColor) {
            mReachedBarColor = progressColor
            mReachedBarPaint.color = mReachedBarColor
            invalidate()
        }
    var progress: Int = 0
        set(progress) {
            if (progress in 0..max) {
                field = progress
                invalidate()
            }
        }
    private var max: Int = 100
        set(maxProgress) {
            if (maxProgress >= 0) {
                field = maxProgress
                invalidate()
            }
        }
    var suffix: String? = ""
    var prefix: String? = ""

    init {
        defaultReachedBarHeight = dp2px(1.5f)
        defaultUnreachedBarHeight = dp2px(1.0f)
        defaultTextSize = sp2px(10f)
        defaultProgressTextOffset = dp2px(3.0f)

        //load styled attributes.
        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.NumberProgressBar,
            defStyleAttr, 0
        )
        mReachedBarColor = attributes.getColor(
            R.styleable.NumberProgressBar_progress_reached_color,
            defaultReachedColor
        )
        mUnreachedBarColor = attributes.getColor(
            R.styleable.NumberProgressBar_progress_unreached_color,
            defaultUnreachedColor
        )
        textColor = attributes.getColor(
            R.styleable.NumberProgressBar_progress_text_color,
            defaultTextColor
        )
        progressTextSize = attributes.getDimension(
            R.styleable.NumberProgressBar_progress_text_size,
            defaultTextSize
        )
        reachedBarHeight = attributes.getDimension(
            R.styleable.NumberProgressBar_progress_reached_bar_height,
            defaultReachedBarHeight
        )
        unreachedBarHeight = attributes.getDimension(
            R.styleable.NumberProgressBar_progress_unreached_bar_height,
            defaultUnreachedBarHeight
        )
        mOffset = attributes.getDimension(
            R.styleable.NumberProgressBar_progress_text_offset,
            defaultProgressTextOffset
        )
        progress = attributes.getInt(R.styleable.NumberProgressBar_progress_current, 0)
        max = attributes.getInt(R.styleable.NumberProgressBar_progress_max, 100)

        mCurIndicatorVisible =
            attributes.getBoolean(R.styleable.NumberProgressBar_progress_current_visibility, true)
        mTextVisible =
            attributes.getBoolean(R.styleable.NumberProgressBar_progress_text_visibility, true)
        mUnReachableBarVisible = attributes.getBoolean(
            R.styleable.NumberProgressBar_progress_unreached_bar_visibility,
            false
        )

        attributes.recycle()
        initializePainters()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false))
    }

    private fun measure(measureSpec: Int, isWidth: Boolean): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        val padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = if (isWidth) suggestedMinimumWidth else suggestedMinimumHeight
            result += padding
            if (mode == MeasureSpec.AT_MOST) {
                result = if (isWidth) {
                    max(result, size)
                } else {
                    min(result, size)
                }
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {

        calculateDrawRectF()
        //ConvertUtils.dp2px(5f).toFloat()  为画笔的圆角宽度
        val x = mReachedRectF.right
        val y = (mReachedRectF.top + mReachedRectF.bottom) / 2
        if (mUnReachableBarVisible)
            canvas.drawLine(
                ConvertUtils.dp2px(5f).toFloat(),
                y,
                width.toFloat() - ConvertUtils.dp2px(5f).toFloat(),
                y,
                mUnreachedBarPaint
            )

        canvas.drawLine(
            mReachedRectF.left,
            y,
            x,
            y,
            mReachedBarPaint
        )

        if (mCurIndicatorVisible)
            canvas.drawCircle(x, y, RADIUS.toFloat(), mTextPaint)
        if (mTextVisible)
            canvas.drawText(
                mCurrentDrawText,
                x - mDrawTextWidth / 2,
                y - RADIUS - ConvertUtils.dp2px(5f),
                mTextPaint
            )
    }

    private fun initializePainters() {
        mUnreachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mUnreachedBarColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = ConvertUtils.dp2px(5f).toFloat()
        }
        mReachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mReachedBarColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = ConvertUtils.dp2px(5f).toFloat()
        }
        mTextPaint.run {
            color = textColor
            textSize = progressTextSize
        }
    }

    private fun calculateDrawRectF() {
        val curPercent = progress * 100 / max
        mCurrentDrawText = mPrefix + String.format("%d", curPercent) + mSuffix
        mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText)

        mReachedRectF.left = paddingLeft.toFloat() + ConvertUtils.dp2px(5f)
        mReachedRectF.right = max(
            paddingLeft.toFloat() + curPercent * (width - paddingRight - paddingLeft) / 100f - ConvertUtils.dp2px(
                5f
            ), mReachedRectF.left + ConvertUtils.dp2px(5f)
        )


        mReachedRectF.top = height / 2.0f - reachedBarHeight / 2.0f
        mReachedRectF.bottom = height / 2.0f + reachedBarHeight / 2.0f

        mDrawTextStart = mReachedRectF.right + mOffset
        if (mDrawTextStart + mDrawTextWidth >= width) {
            mDrawTextStart = width - mDrawTextWidth
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putInt(INSTANCE_TEXT_COLOR, textColor)
        bundle.putFloat(INSTANCE_TEXT_SIZE, progressTextSize)
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, reachedBarHeight)
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, unreachedBarHeight)
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, reachedBarColor)
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, mUnreachedBarColor)
        bundle.putInt(INSTANCE_MAX, max)
        bundle.putInt(INSTANCE_PROGRESS, progress)
        bundle.putString(INSTANCE_SUFFIX, suffix)
        bundle.putString(INSTANCE_PREFIX, prefix)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            textColor = state.getInt(INSTANCE_TEXT_COLOR)
            progressTextSize = state.getFloat(INSTANCE_TEXT_SIZE)
            reachedBarHeight = state.getFloat(INSTANCE_REACHED_BAR_HEIGHT)
            unreachedBarHeight = state.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT)
            mReachedBarColor = state.getInt(INSTANCE_REACHED_BAR_COLOR)
            mUnreachedBarColor = state.getInt(INSTANCE_UNREACHED_BAR_COLOR)
            initializePainters()
            max = state.getInt(INSTANCE_MAX)
            progress = state.getInt(INSTANCE_PROGRESS)
            prefix = state.getString(INSTANCE_PREFIX)
            suffix = state.getString(INSTANCE_SUFFIX)
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }


    private fun dp2px(dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    @Suppress("SameParameterValue")
    private fun sp2px(sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }

    @Suppress("unused")
    fun setOnProgressBarListener(listener: OnProgressBarListener?) {
        mListener = listener
    }

    companion object {
        /**
         * 圆点的半径
         */
        private const val RADIUS = 22

        /**
         * For save and restore instance of progressbar.
         */
        private const val INSTANCE_STATE = "saved_instance"
        private const val INSTANCE_TEXT_COLOR = "text_color"
        private const val INSTANCE_TEXT_SIZE = "text_size"
        private const val INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height"
        private const val INSTANCE_REACHED_BAR_COLOR = "reached_bar_color"
        private const val INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height"
        private const val INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color"
        private const val INSTANCE_MAX = "max"
        private const val INSTANCE_PROGRESS = "progress"
        private const val INSTANCE_SUFFIX = "suffix"
        private const val INSTANCE_PREFIX = "prefix"
    }
}