package com.tcl.base.utils

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.ActivityUtils
import com.tcl.base.R
import com.tcl.base.databinding.AppViewToastBinding

/**
 * desc   : 吐司工具类
 * author : tanksu
 * date   : 2020-12-29
 */
class TxkToastUtil {

    companion object {

        /**
         * 中间展示文字
         * @param msg String
         */
        fun showCentreText(msg: String) {
            showToast(msg, gravity = Gravity.CENTER, showTextOnly = true)
        }

        /**
         * 底部展示文字
         * @param msg String
         */
        fun showBottomText(msg: String) {
            showToast(msg, gravity = Gravity.BOTTOM, yOffset = 200, showTextOnly = true)
        }

        /**
         * 中间警告icon+文字toast
         * @param msg String
         */
        fun showWarning(msg: String) {
            showToast(msg, iconRes = R.mipmap.ic_warnning)
        }

        /**
         * 中间错误icon+文字toast
         * @param msg String
         */
        fun showError(msg: String) {
            showToast(msg, iconRes = R.mipmap.ic_error)
        }

        /**
         * 中间完成icon+文字toast
         * @param msg String
         */
        fun showDone(msg: String) {
            showToast(msg, iconRes = R.mipmap.ic_done)
        }

        var toast: Toast? = null

        @SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
        fun showToast(
            msg: String,
            iconRes: Int = 0,
            @Suppress("UNUSED_PARAMETER") duration: Int = Toast.LENGTH_SHORT,
            gravity: Int = Gravity.CENTER,
            yOffset: Int = 0,
            showTextOnly: Boolean = false
        ) {
            try {
                val binding =
                    AppViewToastBinding.inflate(LayoutInflater.from(ActivityUtils.getTopActivity()))
                val toastView: View = binding.root
                if (showTextOnly) {
                    binding.llToastContainer.apply {
                        background =
                            ActivityUtils.getTopActivity().resources.getDrawable(R.drawable.shape_gray_bg)
                        setPadding(0, 0, 0, 0)
                    }
                }
                if (0 != iconRes) {
                    binding.imgvToastIcon.apply {
                        visibility = View.VISIBLE
                        setImageResource(iconRes)
                    }
                }
                binding.tvToast.text = msg

                if (toast != null) {
                    toast?.cancel()
                    toast = null
                }
                toast = Toast(ActivityUtils.getTopActivity())
                toast?.setGravity(gravity, 0, yOffset)
                @Suppress("DEPRECATION")
                toast?.view = toastView
                toast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}