package com.dream.xiaobei.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.BarUtils
import com.dream.xiaobei.R
import com.dream.xiaobei.databinding.ActivityMainBinding
import com.dream.xiaobei.main.menu.*
import com.dream.xiaobei.utils.StatusBarUtils
import com.google.android.material.tabs.TabLayout
import com.tcl.base.common.ui.BaseActivity
import com.tcl.tclzjpro.main.FixFragmentNavigator

/**
 *@author tiaozi
 *@date   2021/12/8
 *description
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(){
    var lastPos = -1
    var curPos = MAIN_TAB_AREA
    private lateinit var controller: NavController

    init {
        config.isDoubleBack = true
    }

    override fun initStateBar(stateBarColor: Int, isLightMode: Boolean, fakeView: View?) {
        BarUtils.setStatusBarLightMode(this, true)
    }

    override fun initView(savedInstanceState: Bundle?) {
        isFullScreen()
        controller = findNavController(R.id.main_container)
        val fragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navigator = FixFragmentNavigator(this, supportFragmentManager, fragment.id)
        controller.navigatorProvider.addNavigator(navigator)
        controller.setGraph(R.navigation.main_navigation)

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
                R.id.navigation_area -> {
                    curPos = MAIN_TAB_AREA
                }
                R.id.navigation_square -> {
                    curPos = MAIN_TAB_SQUARE
                }
                R.id.navigation_message -> {
                    curPos = MAIN_TAB_MESSAGE
                }
                R.id.navigation_mine -> {
                    curPos = MAIN_TAB_MINE
                }
            }
            mBinding.mainTab.selectTab(mBinding.mainTab.getTabAt(curPos))
        }
    }
    override fun onBlockBackPressed(): Boolean {
        return curPos != MAIN_TAB_AREA
    }

    override fun doOnBlockBackPressed() {
        super.doOnBlockBackPressed()
        findNavController(R.id.main_container).navigate(R.id.navigation_area)
    }
    /**监听新的intent*/
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.run {
            val position = getIntExtra(KEY_TAB_POSITION, 0)
            switchTab(position)
        }
    }
    /**根据下标切换页面*/
    private fun switchTab(curPos: Int) {
        when (curPos) {
            MAIN_TAB_AREA -> controller.navigate(R.id.navigation_area)
            MAIN_TAB_SQUARE -> controller.navigate(R.id.navigation_square)
            MAIN_TAB_MESSAGE -> controller.navigate(R.id.navigation_message)
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