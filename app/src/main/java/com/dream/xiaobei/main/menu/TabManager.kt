package com.dream.xiaobei.main.menu

import androidx.annotation.IntDef
import com.dream.xiaobei.R
import com.dream.xiaobei.common.MmkvConstant
import com.tcl.base.utils.MmkvUtil

/**
 * Author: Liu Feng Wei
 * Date : 2021/6/9
 * Drc:
 */
const val MAIN_TAB_HOME = 0
const val MAIN_TAB_CATEGORY = 1
const val MAIN_TAB_RANK = 2
const val MAIN_TAB_CART = 3
const val MAIN_TAB_MINE = 4
const val KEY_TAB_POSITION = "position"

object TabManager {
    val menus:List<TabItem>
    get() {
       return arrayListOf<TabItem>().apply {
            //活动上线图片
            if (MmkvUtil.decodeBoolean(MmkvConstant.TAP_ICON_CHANGE_KEY) == true) {
                add(TabItem(MAIN_TAB_HOME, R.id.tab_lottie_1, R.string.title_home, "lottie_home_new.json"))
                add(
                    TabItem(
                        MAIN_TAB_CATEGORY,
                        R.id.tab_lottie_2,
                        R.string.title_category,
                        "lottie_category_new.json"
                    )
                )
                add(TabItem(MAIN_TAB_RANK, R.id.tab_lottie_3, R.string.title_rank, "lottie_rank_new.json"))
                add(TabItem(MAIN_TAB_CART, R.id.tab_lottie_4, R.string.title_cart, "lottie_cart_new.json"))
                add(TabItem(MAIN_TAB_MINE, R.id.tab_lottie_5, R.string.title_mine, "lottie_mine_new.json"))
            } else {
                add(TabItem(MAIN_TAB_HOME, R.id.tab_lottie_1, R.string.title_home, "lottie_home.json"))
                add(
                    TabItem(
                        MAIN_TAB_CATEGORY,
                        R.id.tab_lottie_2,
                        R.string.title_category,
                        "lottie_category.json"
                    )
                )
                add(TabItem(MAIN_TAB_RANK, R.id.tab_lottie_3, R.string.title_rank, "lottie_rank.json"))
                add(TabItem(MAIN_TAB_CART, R.id.tab_lottie_4, R.string.title_cart, "lottie_cart.json"))
                add(TabItem(MAIN_TAB_MINE, R.id.tab_lottie_5, R.string.title_mine, "lottie_mine.json"))
            }
        }
    }
}

@IntDef(MAIN_TAB_HOME, MAIN_TAB_CATEGORY, MAIN_TAB_RANK, MAIN_TAB_CART, MAIN_TAB_MINE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TabId