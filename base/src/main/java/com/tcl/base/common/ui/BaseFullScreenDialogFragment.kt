package com.tcl.base.common.ui

/**
 * Author: tiaozi
 * Date : 2021/6/17
 * Drc: 全屏的DialogFragment
 */
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.tcl.base.common.BaseViewModel
import com.tcl.base.common.ViewModelFactory
import com.tcl.base.common.ui.config.FragmentConfig
import java.lang.reflect.ParameterizedType

open class BaseFullScreenDialogFragment<VM : BaseViewModel, VB : ViewBinding>(val config: FragmentConfig = FragmentConfig()) : DialogFragment() {
    protected lateinit var viewModel: VM
    protected lateinit var mBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initBinding(inflater, container)
    }

    /**
     * 使用 DataBinding时,要重写此方法返回相应的布局 id
     * 使用ViewBinding时，不用重写此方法
     */
    open fun layoutId(): Int = 0
    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            return when {
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, inflater) as VB
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

    override fun onStart() {
        super.onStart()
        initWindow()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        lifecycle.addObserver(viewModel)
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
            viewModel = ViewModelProvider(viewModelStore, ViewModelFactory()).get(tClass) as VM
        }
    }

    protected open fun cancelable(): Boolean = true

    private fun initWindow() {
        dialog?.run {
            setCancelable(cancelable())
            window?.run {
                setBackgroundDrawable(ColorDrawable(0x00000000))
                val lp: WindowManager.LayoutParams = attributes
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT
                attributes = lp
                decorView.setPadding(0, 0, 0, 0)
            }
        }
    }


}