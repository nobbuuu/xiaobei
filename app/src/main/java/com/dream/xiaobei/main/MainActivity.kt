package com.dream.xiaobei.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.dream.xiaobei.R
import com.dream.xiaobei.app.UserManager
import com.dream.xiaobei.databinding.ActivityMainBinding
import com.dream.xiaobei.main.menu.*
import com.dream.xiaobei.utils.StatusBarUtils
import com.google.android.material.tabs.TabLayout
import com.tcl.base.common.ui.BaseActivity
import com.tcl.base.utils.MmkvUtil
import com.tcl.tclzjpro.main.FixFragmentNavigator

/**
 *@author tiaozi
 *@date   2021/12/8
 *description
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(){
    var lastPos = -1
    var curPos = MAIN_TAB_HOME
    private lateinit var controller: NavController

    init {
        config.isDoubleBack = true
    }

    override fun initStateBar(stateBarColor: Int, isLightMode: Boolean, fakeView: View?) {
        BarUtils.setStatusBarLightMode(this, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        isFullScreen()
        controller = findNavController(R.id.main_container)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navigator = FixFragmentNavigator(this, supportFragmentManager, fragment.id)
        controller.navigatorProvider.addNavigator(navigator)
        controller.setGraph(R.navigation.mobile_navigation)
        val decryptGet = MmkvUtil.decryptGet("oaid")
        LogUtils.iTag(
            "tanksu",
            "oaid: $decryptGet phone:${UserManager.userInfoBean?.phone}"
        )

        repeat(TabManager.menus.size) {
            val tab = mBinding.mainTab.newTab()
            tab.customView = MainTabView(this, TabManager.menus[it])
            mBinding.mainTab.addTab(tab)
        }

        mBinding.mainTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.run {
                    if (curPos != position) {
                        lastPos = curPos
                        curPos = position
                        switchTab(curPos)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //No-op
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { // No-op
            }
        })
        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    curPos = MAIN_TAB_HOME
                    StatusBarUtils().adjustWindow(this, false)
                }
                R.id.navigation_category -> {
                    curPos = MAIN_TAB_CATEGORY
                    BarUtils.setStatusBarLightMode(this, true)
                }
                R.id.navigation_ranks -> {
                    curPos = MAIN_TAB_RANK
                    BarUtils.setStatusBarLightMode(this, false)
                }
                R.id.navigation_cart -> {
                    curPos = MAIN_TAB_CART
                    BarUtils.setStatusBarLightMode(this, true)
                }
                R.id.navigation_mine -> {
                    curPos = MAIN_TAB_MINE
                }
            }
            mBinding.mainTab.selectTab(mBinding.mainTab.getTabAt(curPos))
        }
    }

    /**根据下标切换页面*/
    private fun switchTab(curPos: Int) {
        when (curPos) {
            MAIN_TAB_HOME -> controller.navigate(R.id.navigation_home)
            MAIN_TAB_CATEGORY -> controller.navigate(R.id.navigation_category)
            MAIN_TAB_RANK -> controller.navigate(R.id.navigation_ranks)
            MAIN_TAB_CART -> controller.navigate(R.id.navigation_cart)
            MAIN_TAB_MINE -> controller.navigate(R.id.navigation_mine)
        }
    }
    override fun onResume() {
        super.onResume()
        val index = mBinding.mainTab.selectedTabPosition
    }

    override fun initData() {

    }

    override fun initDataOnResume() {

    }
}