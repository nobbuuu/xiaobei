package com.tcl.base.common.ui

/**
 * Author: tiaozi
 * Date : 2021/6/3
 * Drc:
 */
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.*
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.tcl.base.R
import com.tcl.base.app.MVVMTcl
import com.tcl.base.common.BaseViewModel
import com.tcl.base.common.ui.config.ActivityConfig
import com.tcl.base.event.Message
import com.tcl.base.kt.ktToastShowInBottom
import com.tcl.base.utils.TxkToastUtil
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding>(var config: ActivityConfig = ActivityConfig()) :
    AppCompatActivity() {

    init {
        config.apply {
            isDoubleBack = false
        }
    }

    protected lateinit var viewModel: VM

    protected lateinit var mBinding: DB

    private var dialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registorDefUIChange()
        initTitleBar()
        initStateBar()
        initView(savedInstanceState)
        startObserve()
        initData()
    }

    override fun onResume() {
        super.onResume()
        initDataOnResume()
    }


    open fun layoutId(): Int = 0
    open fun initStateBar(
        @ColorInt stateBarColor: Int = ColorUtils.getColor(R.color.white),
        isLightMode: Boolean = true,
        fakeView: View? = null
    ) {
        BarUtils.setStatusBarColor(this, stateBarColor)
        BarUtils.setStatusBarLightMode(this, isLightMode)
        fakeView?.run {
            BarUtils.addMarginTopEqualStatusBarHeight(fakeView)
        }
    }

    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    open fun startObserve() {}
    abstract fun initDataOnResume()


    /**
     * DataBinding or ViewBinding
     */
    private fun initViewDataBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            when {
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, layoutInflater) as DB
                        setContentView(mBinding.root)
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    setContentView(layoutId())
                }
            }
            createViewModel(type.actualTypeArguments[0])
        } else throw IllegalArgumentException("Generic error")
    }

    /**
     * 设置activity 全屏，view 不为空时，为 view 增加 MarginTop 为状态栏高度
     */
    fun isFullScreen(view: View? = null) {
        BarUtils.transparentStatusBar(this)
        view?.let {
            BarUtils.addMarginTopEqualStatusBarHeight(view)
        }
    }

    /**
     * 可以复写返回键，标题，右边按钮 的点击事件。
     * 返回键默认实现finish
     */
    fun initTitleBar(
        @IdRes viewId: Int = R.id.titleBar,
        leftBlock: (v: View?) -> Unit = { finish() },
        titleBlock: (v: View?) -> Unit = {},
        rightBlock: (v: View?) -> Unit = {},
        isWhileIcon: Boolean = false
    ) {
        val titleBar = findViewById<TitleBar>(viewId)
        titleBar?.let {
            titleBar.leftIcon = ContextCompat.getDrawable(
                this,
                if (isWhileIcon) R.mipmap.ic_back_white else R.mipmap.ic_black
            )
            titleBar.setOnTitleBarListener(object : OnTitleBarListener {
                override fun onLeftClick(v: View?) {
                    leftBlock.invoke(v)
                }

                override fun onTitleClick(v: View?) {
                    titleBlock.invoke(v)
                }

                override fun onRightClick(v: View?) {
                    rightBlock.invoke(v)
                }
            })
        }
    }


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.defUI.showDialog.observe(this, {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(this, {
            dismissLoading()
        })
        viewModel.defUI.toastEvent.observe(this, {
            TxkToastUtil.showCentreText(it)
        })
        viewModel.defUI.msgEvent.observe(this, {
            handleEvent(it)
        })
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        (dialog ?: MaterialDialog(this)
            .cancelable(false)
            .cornerRadius(8f)
            .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
            .lifecycleOwner(this)
            .maxWidth(R.dimen.dialog_width).also {
                dialog = it
            })
            .show()
    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }


    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel(type: Type) {
        val tClass = type as? Class<VM> ?: BaseViewModel::class.java
        viewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
            .get(tClass) as VM
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return MVVMTcl.getConfig().viewModelFactory
    }

    private var mLastTagTime = 0L
    private val INTERVAL_TIME = 800

    open fun onBlockBackPressed(): Boolean = false
    open fun doOnBlockBackPressed() {}

    override fun onBackPressed() {
        if (onBlockBackPressed()) {
            doOnBlockBackPressed()
        } else if (config.isDoubleBack) {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - mLastTagTime < INTERVAL_TIME) {
                ActivityUtils.finishAllActivities()
//                exitProcess(1)
            } else {
                mLastTagTime = currentTimeMillis
                "再按一次退出".ktToastShowInBottom()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun moveTaskToBack(nonRoot: Boolean): Boolean {
        return super.moveTaskToBack(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtils.eTag("mars", "配置变化：$newConfig")
    }

//    override fun getResources(): Resources {
//        val res = super.getResources()
//        val config = Configuration()
//        config.fontScale = 1f
//        res.updateConfiguration(config, res.displayMetrics)
//        return res
//    }

//    override fun attachBaseContext(newBase: Context) {
//        newBase.resources.configuration.run {
//            LogUtils.eTag("mars", "fontScale=$fontScale")
//            Configuration().apply {
//                setToDefaults()
//                LogUtils.eTag("mars", "默认的fontScale=$fontScale")
//            }
//            if (fontScale != 1.0f) {
//                fontScale = 1.0f
//            }
//            super.attachBaseContext(newBase.createConfigurationContext(this))
//        }
//    }
}