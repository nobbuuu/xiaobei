package com.tcl.base.app

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
object MVVMTcl {
    private lateinit var mConfig: GlobalConfig

    fun install(config: GlobalConfig) {
        mConfig = config
    }

    fun getConfig() = mConfig
}