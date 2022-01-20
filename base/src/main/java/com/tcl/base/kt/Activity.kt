package com.tcl.base.kt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

private var triggerLastTime = 0L

fun <T : Activity> Fragment.ktStartActivity(
    clazz: KClass<T>,
    block: (Intent.() -> Unit)? = null
) {
    activity?.ktStartActivity(clazz, block)
}

fun <T : Activity> Activity.ktStartActivity(
    clazz: KClass<T>,
    block: (Intent.() -> Unit)? = null
) {
//    ktSeriesClick {
        startActivity(Intent(this, clazz.java).apply {
            block?.invoke(this)
        })
//    }
}

fun <T : Context> Context.ktStartActivity(
    clazz: KClass<T>,
    block: (Intent.() -> Unit)? = null
) {
//    ktSeriesClick {
        startActivity(Intent(this, clazz.java).apply {
            block?.invoke(this)
        })
//    }
}

fun <T : Activity> Activity.ktStartActivityAndFinish(
    clazz: KClass<T>,
    block: (Intent.() -> Unit)? = null,
    ) {
    ktStartActivity(clazz, block)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    finish()
}

fun View.ktClick(time: Long = 600, block: () -> Unit?) {
    click {
        ktSeriesClick(time, block)
    }
}

fun <T : Activity> Activity.ktStartActivity4Result(
    clazz: KClass<T>,
    requestCode: Int,
    block: (Intent.() -> Unit)? = null
) {
    ktSeriesClick {
        startActivityForResult(Intent(this, clazz.java).apply {
            block?.invoke(this)
        }, requestCode)
    }
}

/**
 * 防止连点
 */
fun ktSeriesClick(time: Long = 600, block: () -> Unit?) {
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= time) {
        block.invoke()
    }
    triggerLastTime = currentClickTime
}

fun AppCompatActivity.addRecommendFragment(container: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .replace(container, fragment)
        .commitAllowingStateLoss()
}


fun Fragment.addRecommendFragment(container: Int, fragment: Fragment) {
    childFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .replace(container, fragment)
        .commitAllowingStateLoss()
}


