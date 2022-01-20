package com.tcl.base.kt

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.RegexUtils
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.tcl.base.R
import com.tcl.base.utils.AppGlobals
import com.tcl.base.utils.BigDecimalUtils
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * @author : Yzq
 * time : 2020/11/16
 *
 */
const val SET_THEME = "set_theme"
const val MY_PAGE_SET_THEME_COLOR = "my_page_set_theme_color"

@Suppress("unused")
const val HOME_PAGE_CUT = "home_page_cut"
const val MAIN_PLAZA_CUT = "main_plaza_cut"

@Suppress("unused")
const val UPDATE_COLLECT_STATE = "update_collect_state"

//获取包名
fun Context.packageInfo(): PackageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

//获取颜色
fun Context.ktGetColor(colorRes: Int) = ContextCompat.getColor(this, colorRes)
fun View.ktGetColor(colorRes: Int) = context.ktGetColor(colorRes)

//设置颜色
fun Context.text(textRes: Int) = this.resources.getString(textRes)
fun View.text(textRes: Int) = context.text(textRes)

//获取主题属性id
fun TypedValue.resourceId(resId: Int, theme: Resources.Theme): Int {
    theme.resolveAttribute(resId, this, true)
    return this.resourceId
}

//加载子布局
fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = true): View {
    if (layoutId == -1) {
        return this
    }
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun <reified T : ViewModel> NavController.viewModel(@NavigationRes navGraphId: Int): T {
    val storeOwner = getViewModelStoreOwner(navGraphId)
    return ViewModelProvider(storeOwner)[T::class.java]
}

fun String?.htmlToSpanned() =
    if (this.isNullOrEmpty()) "" else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)

/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
@Suppress("unused")
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
@Suppress("unused", "UNCHECKED_CAST")
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param time Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.ktClick(time: Long = 600, block: () -> Unit) {
    triggerDelay = time
    ClickUtils.applyPressedBgDark(this)
    setOnClickListener {
        if (clickEnable()) {
            block()
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else -601
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 600
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

/***
 * 带延迟过滤的点击事件监听，见[View.OnClickListener]
 * 延迟时间根据triggerDelay获取：600毫秒，不能动态设置
 */
interface OnLazyClickListener : View.OnClickListener {

    override fun onClick(v: View?) {
        if (v?.clickEnable() == true) {
            onLazyClick(v)
        }
    }

    fun onLazyClick(v: View)
}

/**
 * 设置Activity的亮度
 * @param [brightness] 0 ~ 1
 */
fun Activity.setBrightness(brightness: Float) {
    val attributes = window.attributes
    attributes.screenBrightness = brightness
    window.attributes = attributes
}

val Float.ktToDp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
val Int.ktToDp
    get() = this.toFloat().ktToDp

val Int.ktDpToPx
    get() = this.toFloat().ktToDp.toInt()

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )
val Int.sp
    get() = this.toFloat().sp
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
val Int.dp
    get() = this.toFloat().dp


val Int.ktToColor
    get() = ContextCompat.getColor(AppGlobals.getApplication(), this)

val Int.toDrawable
    get() = ContextCompat.getDrawable(AppGlobals.getApplication(), this)
val Int.dpToInt
    get() = this.toFloat().ktToDp.toInt()

fun EditText.delayChangedText(time: Long, action: (text: String?) -> Unit) {

    addTextChangedListener(object : TextWatcher {
        val INPUT_MESSAGE_WHAT = 3
        val myHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == INPUT_MESSAGE_WHAT) {
                    val inputContext = msg.obj as String
                    action(inputContext)
                }
            }
        }

        override fun afterTextChanged(text: Editable?) {
            myHandler.removeMessages(INPUT_MESSAGE_WHAT)
            val inputContentMsg = Message.obtain()
            inputContentMsg.what = INPUT_MESSAGE_WHAT
            inputContentMsg.obj = text?.toString()
            myHandler.sendMessageDelayed(inputContentMsg, time)
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            //后期添加延迟回调
//            action(text?.toString())

//            Handler().postDelayed({
//                //doSomethingHere()
//            }, 1000)
        }
    })
}


fun EditText.ktTextWatch(action: (text: String?) -> Unit) {

    addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(text: Editable?) {
            action.invoke(text?.toString())
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/**
 * 改变文字部分颜色
 * @param changeText 需要改变的文字
 * @param colorRes 改变的颜色
 */
fun TextView.ktSetPartTextColor(
    source: String,
    changeText: String,
    colorRes: Int,
    spannableString: SpannableString? = null
) {
    var mSpannableString = spannableString
    if (mSpannableString == null) {
        mSpannableString = SpannableString(source)
    }
    val indexOf = source.indexOf(changeText)
    mSpannableString.setSpan(
        ForegroundColorSpan(colorRes.ktToColor),
        indexOf,
        indexOf + changeText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = mSpannableString
}

/**
 * 改变多个文字颜色
 */
fun TextView.ktSetPartTextColor(
    source: String,
    changeTexts: Array<String>,
    colors: IntArray
) {
    if (changeTexts.size != colors.size) error("changeTexts与colors长度不一致")
    val spannableString = SpannableString(source)
    repeat(changeTexts.size) {
        ktSetPartTextColor(source, changeTexts[it], colors[it], spannableString)
    }
    text = spannableString
}

/**
 * 弹出软键盘
 */
fun View.showSoftInput() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 隐藏软键盘
 */
fun View.hideSoftInput() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Long.toDateTime(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)


///**
// * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
// * @return true-已登录，false-未登录
// */
//fun Context.checkLogin(then: (() -> Unit)? = null): Boolean {
//    return if (isLogin()) {
//        then?.invoke()
//        true
//    } else {
//        this.startActivity<LoginActivity>()
//        false
//    }
//}
//
//fun isLogin(): Boolean = getLoginState() && CookieClass.hasCookie()


//绑定普通的RecyclerView
const val VERTICAL = 0
const val HORIZONTAL = 1
const val GRID = 2
const val FLEX_BOX = 3
fun RecyclerView.ktInit(
    bindAdapter: RecyclerView.Adapter<*>,
    layoutMangerType: Int = VERTICAL,
    spanCount: Int = 3,
    isDecoration: Boolean = false
): RecyclerView {
    layoutManager = when (layoutMangerType) {
        VERTICAL -> LinearLayoutManager(context)
        HORIZONTAL -> LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        GRID -> {
            val gridLayoutManager = GridLayoutManager(context, spanCount)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (bindAdapter.getItemViewType(position) == 0) {
                        return spanCount
                    }
                    return 1
                }
            }
            gridLayoutManager
        }
        FLEX_BOX -> {
            val flexBoxLayoutManger = FlexboxLayoutManager(context)
            //方向 主轴为水平方向，起点在左端
            flexBoxLayoutManger.flexDirection = FlexDirection.ROW
            //左对齐
            flexBoxLayoutManger.justifyContent = JustifyContent.FLEX_START
            flexBoxLayoutManger
        }

        else -> LinearLayoutManager(context)
    }
    adapter = bindAdapter


    if (isDecoration)
        addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))


    return this
}

/**
 * 根据产品线Code，获取对应的产品线名称
 */
fun String.ktChangeProductFamilyName(): String {
    return when (this) {
        "01" -> "电视"
        "02" -> "东芝"
        "03" -> "乐华"
        "04" -> "集成灶"
        "05" -> "雷鸟"
        "06" -> "智能门锁"
        "07" -> "手机"
        "08" -> "空调"
        "09" -> "冰洗"
        else -> "其他"
    }
}

/**
 * 根据ifValue 这个值选择要展示的图片 ： true -》trueResId    false-》 falseResId
 */
fun ImageView.ktSetImageIf(
    ifValue: Boolean,
    @DrawableRes trueResId: Int,
    @DrawableRes falseResId: Int
) {
    this.setImageResource(if (ifValue) trueResId else falseResId)
}


/**
 * View 是不是显示
 */
fun View.ktVisually(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun BaseViewHolder.ktSetInVisible(@IdRes viewId: Int, isInVisible: Boolean): BaseViewHolder {
    val view = getView<View>(viewId)
    view.visibility = if (isInVisible) View.INVISIBLE else View.VISIBLE
    return this
}


fun BaseViewHolder.ktSetImage(@IdRes viewId: Int, imageUrl: String? = ""): BaseViewHolder {
    val iv = getView<ImageView>(viewId)
    var realUrl = imageUrl
    if (realUrl == null) {
        realUrl = ""
    }
    iv.load(realUrl) {
        size(ViewSizeResolver(iv))
        placeholder(R.mipmap.ic_norm_goods)
        error(R.mipmap.ic_norm_goods)
    }
    return this
}


fun Double.ktSetNo0Str(): String {
    return BigDecimal(BigDecimalUtils.formatMoney(this.toString())).stripTrailingZeros()
        .toPlainString()
}


/**
 * 禁止EditText输入特殊字符
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
fun EditText.ktAddEditTextInhibitInputSpeChat() {
    val filter = InputFilter { source, start, end, dest, dstart, dend ->
        val speChat = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！¥…（）—【】‘；：”“’。，、？]"
        val pattern: Pattern = Pattern.compile(speChat)
        val matcher: Matcher = pattern.matcher(source.toString())
        if (matcher.find()) "" else null
    }
    this.filters = arrayOf(filter)
}


fun Double.ktToString(): String {
    val nf: NumberFormat = NumberFormat.getInstance()
    nf.isGroupingUsed = false
    nf.format(this)
    return nf.format(this)
}

var typeFilter =
    InputFilter { source, start, end, dest, dstart, dend ->
        val regx = "^[0-9a-zA-Z\\u4e00-\\u9fa5@\\[\\]【】()（）—#！ !？?/-]+\$"
        if (RegexUtils.isMatch(
                regx,
                source.toString()
            ) || source.isEmpty()
        ) regx else {
            "收货地址存在非法字符".ktToastShow()
            regx
        }
    }


