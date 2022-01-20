package com.tcl.base.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.tcl.base.R
import com.tcl.base.app.MVVMTcl
import com.tcl.base.common.BaseViewModel
import com.tcl.base.common.ui.config.FragmentConfig
import com.tcl.base.event.Message
import com.tcl.base.kt.ktGetColor
import com.tcl.base.utils.TxkToastUtil
import java.lang.reflect.ParameterizedType

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewBinding>(val config: FragmentConfig = FragmentConfig()) :
    Fragment() {

    protected lateinit var viewModel: VM

    protected lateinit var mBinding: DB

    protected lateinit var mRootView: View

    //是否第一次加载
    private var isFirst: Boolean = true

    private var dialog: MaterialDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tempView = initBinding(inflater, container)
        mRootView = if (config.addStateView && tempView is ViewGroup) {
            addStateBar(tempView)
        } else {
            tempView
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        createViewModel()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registerDefUIChange()
        initArgument()
        initView(savedInstanceState)
        startObserve()
        initDataOnViewCreated()
    }

    abstract fun initView(savedInstanceState: Bundle?)
    open fun initArgument() {}
    open fun initDataOnViewCreated() {}
    open fun startObserve() {}

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 使用 DataBinding时,要重写此方法返回相应的布局 id
     * 使用ViewBinding时，不用重写此方法
     */
    open fun layoutId(): Int = 0

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {

        viewModel.defUI.showDialog.observe(viewLifecycleOwner, {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(viewLifecycleOwner, {
            dismissLoading()
        })
        viewModel.defUI.toastEvent.observe(viewLifecycleOwner, {
            TxkToastUtil.showCentreText(it)
        })
        viewModel.defUI.msgEvent.observe(viewLifecycleOwner, {
            handleEvent(it)
        })
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        (dialog ?: MaterialDialog(requireContext())
            .cancelable(false)
            .cornerRadius(8f)
            .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
            .lifecycleOwner(this)
            .maxWidth(R.dimen.dialog_width)
            .also {
                dialog = it
            }).show()
    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }


    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            return when {
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, inflater) as DB
                        mBinding.root
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    inflater.inflate(layoutId(), container, false)
                }
            }
        } else throw IllegalArgumentException("Generic error")
    }

    private fun addStateBar(rootView: ViewGroup): View {
        val linearLayoutCompat = LinearLayoutCompat(requireContext()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        val stateLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            BarUtils.getStatusBarHeight()
        )
        val stateView = View(requireContext()).apply {
            setBackgroundColor(requireContext().ktGetColor(config.stateViewColor))
            layoutParams = stateLayoutParams
        }

        rootView.apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                1.0f
            )
        }
        linearLayoutCompat.addView(stateView)
        linearLayoutCompat.addView(rootView)
        return linearLayoutCompat
    }

    /**
     * 创建 ViewModel
     *
     * 共享 ViewModel的时候，重写  Fragmnt 的 getViewModelStore() 方法，
     * 返回 activity 的  ViewModelStore 或者 父 Fragmnt 的 ViewModelStore
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
                .get(tClass) as VM
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return MVVMTcl.getConfig().viewModelFactory
    }

}