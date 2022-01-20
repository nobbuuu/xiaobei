package com.tcl.base.app

import androidx.lifecycle.ViewModelProvider
import com.tcl.base.common.ViewModelFactory

/**
 * desc   :
 * author : tanksu
 * date   : 2019-11-17
 */
class GlobalConfig {
    var viewModelFactory: ViewModelProvider.NewInstanceFactory = ViewModelFactory()
}