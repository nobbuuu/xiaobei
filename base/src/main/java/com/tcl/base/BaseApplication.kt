package com.tcl.base

import android.app.Application
import android.content.Context
import com.tcl.base.app.GlobalConfig
import com.tcl.base.app.MVVMTcl
import com.tcl.base.common.ViewModelFactory

/**
 * desc   : BaseApplication
 * author : tanksu
 * date   : 2019-11-17
 */
open class BaseApplication : Application() {
    var test = ""
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MVVMTcl.install(GlobalConfig().apply {
            viewModelFactory = ViewModelFactory()
        })
//        base.resources.configuration.run {
//           println("App fontScale=$fontScale")
//            if (fontScale != 1f) {
//                fontScale = 1f
//            }
//            base.resources.updateConfiguration(this, base.resources.displayMetrics)
//            super.attachBaseContext(base.createConfigurationContext(this))
//        }
    }
}