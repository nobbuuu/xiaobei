package com.tcl.tclzjpro.app

import com.dream.xiaobei.BuildConfig

/**
 * desc   : APP设置类
 * author : tanksu
 * date   : 2019-11-17
 */
object Configs {
    private const val H5_DEBUG_MODE = false
    const val APP_TEST_TYPE = "test"
    const val APP_UAT_TYPE = "uat"
    const val APP_PRODUCT_TYPE = "product"
    var curAppType: String = BuildConfig.APP_TYPE

    fun isPackageProductType() = BuildConfig.APP_TYPE == APP_PRODUCT_TYPE

    /**后台 的URL链接*/
    private const val URL_APP_PRODUCT = "https://fxapi.tcl.com"
    private const val URL_APP_UAT = "https://prepc.tclo2o.cn"
    //private const val URL_APP_TEST = "http://10.120.40.106:8082"
    private const val URL_APP_TEST = "https://testpc.tclo2o.cn"

    /**HTML 的URL链接*/
    private const val URL_HTML_APP_PRODUCT = "https://m.tcl.com/seller-app-h5"
    private const val URL_HTML_APP_UAT = "https://prewap.tclo2o.cn/seller-app-h5"
    var URL_HTML_APP_TEST = "https://testwap.tclo2o.cn/seller-app-h5"

    //测试调试链接
    private const val URL_H5_DEBUG = "http://10.120.42.124:8080/seller-app-h5"
    var WEB_URL_OFFICIAL = "https://service2.tcl.com/auth?apps=thome"
    var WEB_URL_OFFICIAL_TEST = "https://service-test2.koyoo.cn/auth?apps=thome"

    /**获取APP连接到后台的url*/
    fun getAppBaseUrl(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                URL_APP_TEST
            APP_UAT_TYPE ->
                URL_APP_UAT
            APP_PRODUCT_TYPE ->
                URL_APP_PRODUCT
            else -> URL_APP_PRODUCT
        }
    }

    /**获取APP连接到后台的url*/
    fun getAppHtmlBaseUrl(): String {
        if (curAppType != APP_PRODUCT_TYPE && H5_DEBUG_MODE) return URL_H5_DEBUG
        return when (curAppType) {
            APP_TEST_TYPE ->
                URL_HTML_APP_TEST
            APP_UAT_TYPE ->
                URL_HTML_APP_UAT
            APP_PRODUCT_TYPE ->
                URL_HTML_APP_PRODUCT
            else -> URL_HTML_APP_PRODUCT
        }
    }

    /**连接到客户中心的URL 开始*/
    /**后台 的URL链接*/
    private const val URL_ACCOUNT_CENTRE_PRODUCT = "https://cn.account.tcl.com"
    private const val URL_ACCOUNT_CENTRE_UAT = "https://account-uat.tcljd.com"
    private const val URL_ACCOUNT_CENTRE_TEST = "https://account-dev.tcljd.com"
    private const val URL_ACCOUNT_CENTRE_SIT = "https://account-sit.tcljd.com"

    /**获取客户中心的后台url*/
      fun getAccountCentreBaseUrl(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                URL_ACCOUNT_CENTRE_SIT
            APP_UAT_TYPE ->
                URL_ACCOUNT_CENTRE_UAT
            APP_PRODUCT_TYPE ->
                URL_ACCOUNT_CENTRE_PRODUCT
            else -> URL_ACCOUNT_CENTRE_PRODUCT
        }
    }
    /**连接到客户中心的URL 结束*/


    /**TCL的appid等配置信息*/

    private const val TCL_APP_ID_PRODUCT = "57051623737084743"
    private const val TCL_APP_ID_DEV = "84601623736956822"
    private const val TCL_APP_ID_SIT = "37011623737219069"
    private const val TCL_APP_ID_UAT = "66191623737327854"

    /**获取APP的appid*/
    fun getTclAppId(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                TCL_APP_ID_SIT
            APP_UAT_TYPE ->
                TCL_APP_ID_UAT
            APP_PRODUCT_TYPE ->
                TCL_APP_ID_PRODUCT
            else -> TCL_APP_ID_PRODUCT
        }
    }

    private const val TCL_APP_SECRET_PRODUCT =
        "fbb55ccb4f95dcd611a3b2f5518937cec2b84988a8627703e3cb28728181aa1d"
    private const val TCL_APP_SECRET_DEV =
        "7803d548944e4e0cb26f5d032496a64b653ba11ddea54a3ca44f4f6e5b454907"
    private const val TCL_APP_SECRET_SIT =
        "e545f8fbf9c659823e4a0b14383a6098a89cdb7da20122724efc0ce704e7a8b9"
    private const val TCL_APP_SECRET_UAT =
        "650eb6547a8386b8a9dfa81d51be922b7e571583ecdd0cc6a3ec61f1eefcb573"

    /**获取APP的AppSecret*/
    fun getTclAppSecret(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                TCL_APP_SECRET_SIT
            APP_UAT_TYPE ->
                TCL_APP_SECRET_UAT
            APP_PRODUCT_TYPE ->
                TCL_APP_SECRET_PRODUCT
            else -> TCL_APP_SECRET_PRODUCT
        }
    }

    private const val TCL_TANNAN_ID = "tcl"

    /**获取APP的tanantId*/
    fun getTclTenantId(): String = TCL_TANNAN_ID

    private const val BUGLY_APP_ID_TEST = "a8ca9d231f"
    private const val BUGLY_APP_ID_PRODUCT = "fd92522c47"

    fun getBuglyAppid(): String {
        return when (curAppType) {
            APP_TEST_TYPE, APP_UAT_TYPE ->
                BUGLY_APP_ID_TEST
            APP_PRODUCT_TYPE ->
                BUGLY_APP_ID_PRODUCT
            else -> BUGLY_APP_ID_PRODUCT
        }
    }

    /**微信和小程序设置相关 开始*************************************************/
    const val WECHAT_APP_ID = "wx49c01c1024eafb34"
    const val WECHAT_APP_SECRET = "49fd49803e9bfb988ea4ba07608c91c3"

    /**Appid：wxb6f0a6acf00a7b03 VS 小程序测试id：gh_0063dac8f1f0
     *
     * T分销商城（测试版） wxb6f0a6acf00a7b03 gh_0063dac8f1f0
    TCL之家Pro（正式版） wxe18f6ce612abe439 gh_fd128a32a43e
    这2个APP分享都是需要测试的呀  先测T分销商城
     * */
    const val WECHAT_MINI_PROGRAM_ID_TEST = "gh_0063dac8f1f0"
    const val WECHAT_MINI_PROGRAM_ID_PRD = "gh_fd128a32a43e"
    const val WECHAT_APP_ID_MINI_PROGRAM_TEST = "wxb6f0a6acf00a7b03"

    const val ACTION_WECHAT_PAY_RESULT = "com.tcl.tsales.ACTION_WECHATE_PAY_RESULT"
    fun getMiniGramId(): String =
        if (curAppType == APP_PRODUCT_TYPE) WECHAT_MINI_PROGRAM_ID_PRD else WECHAT_MINI_PROGRAM_ID_TEST
    /**微信和小程序设置相关 结束*************************************************/


    /**七鱼APPID*/
    const val KEFU_APP_ID = "56ba1978d5714a75896dff78d07b9c0c" //客服appid

    /**官方客服*/
    fun getOfficialServices(): String =
        if (curAppType != APP_PRODUCT_TYPE) WEB_URL_OFFICIAL_TEST else WEB_URL_OFFICIAL


    /** 埋点相关需求  */
    /** 埋点URL  */
    private const val TCL_POINT_URL_PRODUCT = "https://crumb.tclo2o.cn/api/collect/sdk/upload"
    private const val TCL_POINT_URL_TEST = "https://crumb.tclo2o.cn/api/collect/sdk/upload"
    private const val TCL_POINT_URL_UAT = "https://crumb.tclo2o.cn/api/collect/sdk/upload"

    fun getTclPointURL(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                TCL_POINT_URL_TEST
            APP_UAT_TYPE ->
                TCL_POINT_URL_UAT
            APP_PRODUCT_TYPE ->
                TCL_POINT_URL_PRODUCT
            else -> TCL_POINT_URL_PRODUCT
        }.let { "$it?appId=${getTclPointAppId()}&appToken=${getTclPointAppToken()}" }
    }

    /** 埋点ID  */
    private const val TCL_POINT_APP_ID_PRODUCT = "6"
    private const val TCL_POINT_APP_ID_TEST = "9"
    private const val TCL_POINT_APP_ID_UAT = "11"

    fun getTclPointAppId(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                TCL_POINT_APP_ID_TEST
            APP_UAT_TYPE ->
                TCL_POINT_APP_ID_UAT
            APP_PRODUCT_TYPE ->
                TCL_POINT_APP_ID_PRODUCT
            else -> TCL_POINT_APP_ID_PRODUCT
        }
    }

    /** 埋点Token */
    private const val TCL_POINT_APP_TOKEN_PRODUCT = "MGYxN2U3Nzg0ZmRiNDM2ZmEwMzhhYWRkYThmOTYwNTE="
    private const val TCL_POINT_APP_TOKEN_TEST = "NDYzNjRmMDU5MzQxNDZkNzhlMTUzNDNmODM1NjA0OTI="
    private const val TCL_POINT_APP_TOKEN_UAT = "Y2RhNmZiYjVlMWI4NDI4NjhmZTE0MmE3M2Q4NTVkODM="

    fun getTclPointAppToken(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                TCL_POINT_APP_TOKEN_TEST
            APP_UAT_TYPE ->
                TCL_POINT_APP_TOKEN_UAT
            APP_PRODUCT_TYPE ->
                TCL_POINT_APP_TOKEN_PRODUCT
            else -> TCL_POINT_APP_TOKEN_PRODUCT
        }
    }

    /** 埋点相关需求  */



    private const val T_SHOP_PUBLIC_KEY_PRODUCT = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlAJUwdztsQ7qKkEAK2VTPLbwtAzI8HIeU2REwx+9d1YpFxJRnrHXw1vTOAYpo7UQo4W6BcRX+8Qv0msj/u9JKdZKVle1GE+b7Qb94F7RqEvAeiOtkKF/41+LiBneK7QYAerF0Kwu/I0pBkJujQlmoQM3OIaymqrpwznfuFuMdPLKNH+xcg8DCEYBtET66GugXm0lMbA99K/Tf6UWUcPTk4Svcu9cymE6EiuXMUUH47BpmzsN4hUaj77b/Gl8r3QWoabi2Fu1x2xjEQpChCZFiDcFmtYxGzVeXaBsQN3qL1+1D2/lAdyc4OplKB8IpM+hr7diY7Ozc81mns+2xnsrbQIDAQAB"
    private const val T_SHOP_PUBLIC_KEY_TEST = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjye8XToIQFwNkpHSdDeTNQpmo9Vul2FfgMakt09C5WpyaTolbn82gdBYeUayOJIjzWytgM05oMdIc8wmSAyNZPe8UNpHJBJDDGcl41A4gUAsG1emOX3a0s+jKS9hfExsxKQvte83Gtt25TLAI5xm91tb7BzIvXT2mCrMLWCj6E6LcMwPE8gpMTEZJzTomoQq4qrHAxw4XcASjrVZEaJzzwxACVFh1uUg+Z9WE7z34UZ7BdfFS2rnL1y6h63pGYtLVYXwop54Gam6C/zDcHneDjC9jYyWSwKKclYLrFMhKQMXqlf232DO3CWZZb2DlPaUM1bSu8q2eVvWkPGP02ZP5wIDAQAB"
    private const val T_SHOP_PUBLIC_KEY_UAT = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAld9AozhSZNupdvOya3uG//NUT5e3xUC/1TzAoZuoJ7W+tW4sT5THToq5ZouxP+oI/zIAuY6sHfVas+UU0WYoK+EdQEtijDpydG28uZh2EpufN2KiLHuk2Rsl23shNRFghEGEz9ToOuGgcdrw/e1NWQZse3mKWMaXhg9vaEfIdBX+nmLVy8m02+Bj/xqSVSfEUStAxjTSy9DJxvdBh4BJkdZdCMhFljEZa7ZhBIHyQh5oYfmvSUrfcuSlCcaf/KsmCGCVASrTj730uvauhxbmikhMcpcvbKgJOlCf+0eRP7LpR3kq3IMe8rOqEMpCCVEwcNtaI8aK77dSbXUesG/y7QIDAQAB"

    fun getTShopPKey(): String {
        return when (curAppType) {
            APP_TEST_TYPE ->
                T_SHOP_PUBLIC_KEY_TEST
            APP_UAT_TYPE ->
                T_SHOP_PUBLIC_KEY_UAT
            APP_PRODUCT_TYPE ->
                T_SHOP_PUBLIC_KEY_PRODUCT
            else -> T_SHOP_PUBLIC_KEY_PRODUCT
        }
    }


}