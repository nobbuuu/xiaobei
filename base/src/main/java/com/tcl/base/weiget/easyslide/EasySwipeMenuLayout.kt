package com.tcl.base.weiget.easyslide


import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller
import com.tcl.base.R
import java.util.*


/**
 * Created by guanaj on 2017/6/5.
 */
class EasySwipeMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ViewGroup(context, attrs, defStyleAttr) {
    private val mMatchParentChildren = ArrayList<View>(1)
    private var mLeftViewResID = 0
    private var mRightViewResID = 0
    private var mContentViewResID = 0
    private var mLeftView: View? = null
    private var mRightView: View? = null
    private var mContentView: View? = null
    private var mContentViewLp: MarginLayoutParams? = null
    private var isSwipeing = false
    private var mLastP: PointF? = null
    private var mFirstP: PointF? = null
    var fraction = 0.3f
    var isCanLeftSwipe = true
    var isCanRightSwipe = true
    private var mScaledTouchSlop = 0
    private var mScroller: Scroller? = null
    private val distanceX = 0f
    private var finalyDistanceX = 0f

    /**
     * 初始化方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        //创建辅助对象
        val viewConfiguration = ViewConfiguration.get(context)
        mScaledTouchSlop = viewConfiguration.scaledTouchSlop
        mScroller = Scroller(context)
        //1、获取配置的属性值
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EasySwipeMenuLayout,
            defStyleAttr,
            0
        )
        try {
            val indexCount = typedArray.indexCount
            for (i in 0 until indexCount) {
                val attr = typedArray.getIndex(i)
                if (attr == R.styleable.EasySwipeMenuLayout_leftMenuView) {
                    mLeftViewResID =
                        typedArray.getResourceId(R.styleable.EasySwipeMenuLayout_leftMenuView, -1)
                } else if (attr == R.styleable.EasySwipeMenuLayout_rightMenuView) {
                    mRightViewResID =
                        typedArray.getResourceId(R.styleable.EasySwipeMenuLayout_rightMenuView, -1)
                } else if (attr == R.styleable.EasySwipeMenuLayout_contentView) {
                    mContentViewResID =
                        typedArray.getResourceId(R.styleable.EasySwipeMenuLayout_contentView, -1)
                } else if (attr == R.styleable.EasySwipeMenuLayout_canLeftSwipe) {
                    isCanLeftSwipe =
                        typedArray.getBoolean(R.styleable.EasySwipeMenuLayout_canLeftSwipe, true)
                } else if (attr == R.styleable.EasySwipeMenuLayout_canRightSwipe) {
                    isCanRightSwipe =
                        typedArray.getBoolean(R.styleable.EasySwipeMenuLayout_canRightSwipe, true)
                } else if (attr == R.styleable.EasySwipeMenuLayout_fraction) {
                    fraction = typedArray.getFloat(R.styleable.EasySwipeMenuLayout_fraction, 0.5f)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取childView的个数
        isClickable = true
        var count = childCount
        //参考frameLayout测量代码
        val measureMatchParentChildren =
            MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                    MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
        mMatchParentChildren.clear()
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0
        //遍历childViews
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                val lp = child.layoutParams as MarginLayoutParams
                maxWidth = Math.max(
                    maxWidth,
                    child.measuredWidth + lp.leftMargin + lp.rightMargin
                )
                maxHeight = Math.max(
                    maxHeight,
                    child.measuredHeight + lp.topMargin + lp.bottomMargin
                )
                childState = combineMeasuredStates(childState, child.measuredState)
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                        lp.height == LayoutParams.MATCH_PARENT
                    ) {
                        mMatchParentChildren.add(child)
                    }
                }
            }
        }
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)
        setMeasuredDimension(
            resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                childState shl MEASURED_HEIGHT_STATE_SHIFT
            )
        )
        count = mMatchParentChildren.size
        if (count > 1) {
            for (i in 0 until count) {
                val child = mMatchParentChildren[i]
                val lp = child.layoutParams as MarginLayoutParams
                val childWidthMeasureSpec: Int
                childWidthMeasureSpec = if (lp.width == LayoutParams.MATCH_PARENT) {
                    val width = Math.max(
                        0, measuredWidth
                                - lp.leftMargin - lp.rightMargin
                    )
                    MeasureSpec.makeMeasureSpec(
                        width, MeasureSpec.EXACTLY
                    )
                } else {
                    getChildMeasureSpec(
                        widthMeasureSpec,
                        lp.leftMargin + lp.rightMargin,
                        lp.width
                    )
                }
                val childHeightMeasureSpec: Int
                childHeightMeasureSpec = if (lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    val height = Math.max(
                        0, measuredHeight
                                - lp.topMargin - lp.bottomMargin
                    )
                    MeasureSpec.makeMeasureSpec(
                        height, MeasureSpec.EXACTLY
                    )
                } else {
                    getChildMeasureSpec(
                        heightMeasureSpec,
                        lp.topMargin + lp.bottomMargin,
                        lp.height
                    )
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val left = 0 + paddingLeft
        val right = 0 + paddingLeft
        val top = 0 + paddingTop
        val bottom = 0 + paddingTop
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (mLeftView == null && child.id == mLeftViewResID) {
                // Log.i(TAG, "找到左边按钮view");
                mLeftView = child
                mLeftView!!.isClickable = true
            } else if (mRightView == null && child.id == mRightViewResID) {
                // Log.i(TAG, "找到右边按钮view");
                mRightView = child
                mRightView!!.isClickable = true
            } else if (mContentView == null && child.id == mContentViewResID) {
                // Log.i(TAG, "找到内容View");
                mContentView = child
                mContentView!!.isClickable = true
            }
        }
        //布局contentView
        var cRight = 0
        if (mContentView != null) {
            mContentViewLp = mContentView!!.layoutParams as MarginLayoutParams
            val cTop = top + mContentViewLp!!.topMargin
            val cLeft = left + mContentViewLp!!.leftMargin
            cRight = left + mContentViewLp!!.leftMargin + mContentView!!.measuredWidth
            val cBottom = cTop + mContentView!!.measuredHeight
            mContentView!!.layout(cLeft, cTop, cRight, cBottom)
        }
        if (mLeftView != null) {
            val leftViewLp = mLeftView!!.layoutParams as MarginLayoutParams
            val lTop = top + leftViewLp.topMargin
            val lLeft =
                0 - mLeftView!!.measuredWidth + leftViewLp.leftMargin + leftViewLp.rightMargin
            val lRight = 0 - leftViewLp.rightMargin
            val lBottom = lTop + mLeftView!!.measuredHeight
            mLeftView!!.layout(lLeft, lTop, lRight, lBottom)
        }
        if (mRightView != null) {
            val rightViewLp = mRightView!!.layoutParams as MarginLayoutParams
            val lTop = top + rightViewLp.topMargin
            val lLeft = mContentView!!.right + mContentViewLp!!.rightMargin + rightViewLp.leftMargin
            val lRight = lLeft + mRightView!!.measuredWidth
            val lBottom = lTop + mRightView!!.measuredHeight
            mRightView!!.layout(lLeft, lTop, lRight, lBottom)
        }
    }

    var result: State? = null
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {

                //   System.out.println(">>>>dispatchTouchEvent() ACTION_DOWN");
                isSwipeing = false
                if (mLastP == null) {
                    mLastP = PointF()
                }
                mLastP!![ev.rawX] = ev.rawY
                if (mFirstP == null) {
                    mFirstP = PointF()
                }
                mFirstP!![ev.rawX] = ev.rawY
                if (viewCache != null) {
                    if (viewCache !== this) {
                        viewCache!!.handlerSwipeMenu(State.CLOSE)
                    }
                    // Log.i(TAG, ">>>有菜单被打开");
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_MOVE -> {

                //   System.out.println(">>>>dispatchTouchEvent() ACTION_MOVE getScrollX:" + getScrollX());
                val distanceX = mLastP!!.x - ev.rawX
                val distanceY = mLastP!!.y - ev.rawY
                if (Math.abs(distanceY) > mScaledTouchSlop && Math.abs(distanceY) > Math.abs(
                        distanceX
                    )
                ) {
                    return super.dispatchTouchEvent(ev)
                }
                //                if (Math.abs(distanceX) <= mScaledTouchSlop){
//                    break;
//                }
                // Log.i(TAG, ">>>>>distanceX:" + distanceX);
                scrollBy(distanceX.toInt(), 0) //滑动使用scrollBy
                //越界修正
                if (scrollX < 0) {
                    if (!isCanRightSwipe || mLeftView == null) {
                        scrollTo(0, 0)
                    } else { //左滑
                        if (scrollX < mLeftView!!.left) {
                            scrollTo(mLeftView!!.left, 0)
                        }
                    }
                } else if (scrollX > 0) {
                    if (!isCanLeftSwipe || mRightView == null) {
                        scrollTo(0, 0)
                    } else {
                        if (scrollX > mRightView!!.right - mContentView!!.right - mContentViewLp!!.rightMargin) {
                            scrollTo(
                                mRightView!!.right - mContentView!!.right - mContentViewLp!!.rightMargin,
                                0
                            )
                        }
                    }
                }
                //当处于水平滑动时，禁止父类拦截
                if (Math.abs(distanceX) > mScaledTouchSlop //                        || Math.abs(getScrollX()) > mScaledTouchSlop
                ) {
                    //  Log.i(TAG, ">>>>当处于水平滑动时，禁止父类拦截 true");
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                mLastP!![ev.rawX] = ev.rawY
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                //     System.out.println(">>>>dispatchTouchEvent() ACTION_CANCEL OR ACTION_UP");
                finalyDistanceX = mFirstP!!.x - ev.rawX
                if (Math.abs(finalyDistanceX) > mScaledTouchSlop) {
                    //  System.out.println(">>>>P");
                    isSwipeing = true
                }
                result = isShouldOpen(scrollX)
                handlerSwipeMenu(result)
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        //  Log.d(TAG, "<<<<dispatchTouchEvent() called with: " + "ev = [" + event + "]");
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {

                //滑动时拦截点击时间
                if (Math.abs(finalyDistanceX) > mScaledTouchSlop) {
                    // 当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    //   Log.i(TAG, "<<<onInterceptTouchEvent true");
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                //滑动后不触发contentView的点击事件
                if (isSwipeing) {
                    isSwipeing = false
                    finalyDistanceX = 0f
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    /**
     * 自动设置状态
     *
     * @param result
     */
    private fun handlerSwipeMenu(result: State?) {
        if (result === State.LEFTOPEN) {
            mScroller!!.startScroll(scrollX, 0, mLeftView!!.left - scrollX, 0)
            viewCache = this
            mStateCache = result
        } else if (result === State.RIGHTOPEN) {
            viewCache = this
            mScroller!!.startScroll(
                scrollX,
                0,
                mRightView!!.right - mContentView!!.right - mContentViewLp!!.rightMargin - scrollX,
                0
            )
            mStateCache = result
        } else {
            mScroller!!.startScroll(scrollX, 0, -scrollX, 0)
            viewCache = null
            mStateCache = null
        }
        invalidate()
    }

    override fun computeScroll() {
        //判断Scroller是否执行完毕：
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, mScroller!!.currY)
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate()
        }
    }

    /**
     * 根据当前的scrollX的值判断松开手后应处于何种状态
     *
     * @param
     * @param scrollX
     * @return
     */
    private fun isShouldOpen(scrollX: Int): State? {
        if (mScaledTouchSlop >= Math.abs(finalyDistanceX)) {
            return mStateCache
        }
        Log.i(TAG, ">>>finalyDistanceX:$finalyDistanceX")
        if (finalyDistanceX < 0) {
            //➡滑动
            //1、展开左边按钮
            //获得leftView的测量长度
            if (getScrollX() < 0 && mLeftView != null) {
                if (Math.abs(mLeftView!!.width * fraction) < Math.abs(getScrollX())) {
                    return State.LEFTOPEN
                }
            }
            //2、关闭右边按钮
            if (getScrollX() > 0 && mRightView != null) {
                return State.CLOSE
            }
        } else if (finalyDistanceX > 0) {
            //⬅️滑动
            //3、开启右边菜单按钮
            if (getScrollX() > 0 && mRightView != null) {
                if (Math.abs(mRightView!!.width * fraction) < Math.abs(getScrollX())) {
                    return State.RIGHTOPEN
                }
            }
            //关闭左边
            if (getScrollX() < 0 && mLeftView != null) {
                return State.CLOSE
            }
        }
        return State.CLOSE
    }

    override fun onDetachedFromWindow() {
        if (this === viewCache) {
            viewCache!!.handlerSwipeMenu(State.CLOSE)
        }
        super.onDetachedFromWindow()
        //  Log.i(TAG, ">>>>>>>>onDetachedFromWindow");
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (this === viewCache) {
            viewCache!!.handlerSwipeMenu(mStateCache)
        }
        // Log.i(TAG, ">>>>>>>>onAttachedToWindow");
    }

    fun resetStatus() {
        if (viewCache != null) {
            if (mStateCache != null && mStateCache !== State.CLOSE && mScroller != null) {
                mScroller!!.startScroll(viewCache!!.scrollX, 0, -viewCache!!.scrollX, 0)
                viewCache!!.invalidate()
                viewCache = null
                mStateCache = null
            }
        }
    }

    //➡滑动
    private val isLeftToRight: Boolean
        private get() = if (distanceX < 0) {
            //➡滑动
            true
        } else {
            false
        }

    companion object {
        private const val TAG = "EasySwipeMenuLayout"
        var viewCache: EasySwipeMenuLayout? = null
            private set
        private var mStateCache: State? = null
        val stateCache: State?
            get() = mStateCache
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}
