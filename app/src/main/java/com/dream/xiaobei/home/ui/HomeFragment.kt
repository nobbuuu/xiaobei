package com.dream.xiaobei.home.ui

import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.dream.xiaobei.databinding.FragmentHomeBinding
import com.dream.xiaobei.home.vm.AreaViewModel
import com.tcl.base.common.ui.BaseFragment

/**
 *@author tiaozi
 *@date   2022/1/26
 *description
 */
class HomeFragment : BaseFragment<AreaViewModel, FragmentHomeBinding> (){

    override fun initView(savedInstanceState: Bundle?) {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.homeTitleBar)

    }
}