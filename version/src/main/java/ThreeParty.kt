/**
 * @author tiaozi
 * @date   2021/12/08
 * description
 */
@Suppress("unused")
object ThreeParty {
    const val qiyuSDK = "com.qiyukf.unicorn:unicorn:+"
    const val imageLoader = "com.nostra13.universalimageloader:universal-image-loader:1.9.5"
    const val quicklogin = "io.github.yidun:quicklogin:3.0.6.5"
    const val XPopUp = "com.lxj:xpopup:2.2.23"
    const val Okio = "com.squareup.okio:okio:3.0.0-alpha.4"
    const val OkHttp = "com.squareup.okhttp3:okhttp:4.9.0"
    const val fastJson = "com.alibaba:fastjson:1.2.75"
    const val permissionSupportByGuoLin = "com.permissionx.guolindev:permission-support:1.4.0"
    const val luBan = "top.zibin:Luban:1.1.8"
    const val sensorDataSdk = "com.sensorsdata.analytics.android:SensorsAnalyticsSDK:5.4.3"
    //https://github.com/KingSwim404/KingSwimMock#readme
    const val dataMock = "com.github.KingSwim404:KingSwimMock:1.0.1"

    val rxhttp = RxHttp
    val tencent = Tencent
    val mdDialog = MdDialog
    val tingYun = TingYun
    val dokit = Dokit
    const val PHUONGHUYNH_COMPRESSOR = "com.github.phuonghuynh:compressor:1.13"
    const val Glide = "com.github.bumptech.glide:glide:4.11.0"
    const val GlideCompiler = "com.github.bumptech.glide:compiler:4.11.0"

    //状态栏管理   https://github.com/gyf-dev/ImmersionBar
    const val immersionbar = "com.gyf.immersionbar:immersionbar:3.0.0"

    //播放Svg 动画   https://github.com/airbnb/lottie-android
    const val LOTTIE = "com.airbnb.android:lottie:3.7.0"

    // utils 集合了大量常用的工具
    const val UTILCODEX = "com.blankj:utilcodex:1.30.4"

    //状态页管理 MultiStatePage
    //https://github.com/Zhao-Yan-Yan/MultiStatePage
    const val MULTI_STATE_PAGE = "com.github.Zhao-Yan-Yan:MultiStatePage:2.0.1"

    //仿Android的图片选择框架
    //https://github.com/LuckSiege/PictureSelector/blob/master/README_CN.md
    const val PICTURE_SELECTOR = "com.github.LuckSiege.PictureSelector:picture_library:v2.5.9"

    //指示器
    //https://github.com/hackware1993/MagicIndicator
    const val MagicIndicator = "com.github.hackware1993:MagicIndicator:1.7.0"

    //轮播图
    //https://github.com/zhpanvip/BannerViewPager/blob/master/README_CN.md
    const val BannerViewPager = "com.github.zhpanvip:BannerViewPager:3.5.2"
    const val viewpagerindicator = "com.github.zhpanvip:viewpagerindicator:1.0.5"

    //代替shape ， 其他状态 ViewHelper
    //https://github.com/RuffianZhong/RWidgetHelper
    const val RWidgetHelper = "com.ruffian.library:RWidgetHelper-AndroidX:0.0.6"

    //屏幕适配
    //https://github.com/JessYanCoding/AndroidAutoSize
    const val AutoSize = "me.jessyan:autosize:1.2.1"

    //https://github.com/scwang90/SmartRefreshLayout
    val smartRefresh = SmartRefresh

    //跑马灯
    //https://github.com/gongwen/MarqueeViewLibrary
    const val MarqueeView = "com.sunfusheng:MarqueeView:1.4.1"

    // 标题栏框架：https://github.com/getActivity/TitleBar
    const val titleBar = "com.github.getActivity:TitleBar:8.6"
    const val baseRecyclerViewAdapterHelper =
        "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4"

    //LiveEventBus是一款Android消息总线，基于LiveData，具有生命周期感知能力，支持Sticky，支持AndroidX，支持跨进程，支持跨APP
    // https://github.com/JeremyLiao/LiveEventBus
    const val liveEventBus = "com.jeremyliao:live-event-bus-x:1.7.2"

    //图片压缩
    const val compressor = "id.zelory:compressor:2.1.0"

    // 悬浮窗框架：https://github.com/getActivity/XToast
    const val xToast = "com.hjq:xtoast:6.6"

    // ConsecutiveScrollerLayout是Android下支持多个滑动布局：https://github.com/donkingliang/ConsecutiveScroller/wiki
    const val ConsecutiveScrollerLayout = "com.github.donkingliang:ConsecutiveScroller:4.5.0"

    //日志
    val geTui = GeTuiPush
    const val gsyVideoPlayer = "com.shuyu:GSYVideoPlayer:8.0.0"

    const val easySwipeMenuLayout = "com.github.anzaizai:EasySwipeMenuLayout:1.1.4"

    const val ultraViewPager = "com.alibaba.android:ultraviewpager:1.0.7.8"

    object RxHttp {
        private const val version = "2.5.5"

        // https://github.com/liujingxing/okhttp-RxHttp，
        const val base = "com.ljx.rxhttp:rxhttp:$version"
        const val compiler = "com.ljx.rxhttp:rxhttp-compiler:$version"

        //    kapt 'com.ljx.rxhttp:rxhttp-compiler:2.4.1' //生成RxHttp类，纯Java项目，请使用annotationProcessor代替kapt
        //https://github.com/liujingxing/RxLife-Coroutine
        //管理协程生命周期，页面销毁，关闭请求
        const val coroutine = "com.ljx.rxlife:rxlife-coroutine:2.0.1"
    }

    object Tencent {
        const val Bugly = "com.tencent.bugly:crashreport:2.2.0"
        const val BuglyNative = "com.tencent.bugly:nativecrashreport:latest.release"
        const val Mmkv = "com.tencent:mmkv-static:1.2.4"
        const val WeChatSdk = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+"

        //腾讯x5webview https://x5.tencent.com/docs/access.html
        const val X5WebView = "com.tencent.tbs.tbssdk:sdk:43939"
    }

    object MdDialog {
        const val materialDialogsLifecycle = "com.afollestad.material-dialogs:lifecycle:3.3.0"
        const val materialDialogsCore = "com.afollestad.material-dialogs:core:3.3.0"
    }

    object SmartRefresh {
        const val kernel = "com.scwang.smart:refresh-layout-kernel:2.0.1"      //核心必须依赖
        const val header = "com.scwang.smart:refresh-header-material:2.0.1"    //谷歌刷新头
        const val footer = "com.scwang.smart:refresh-footer-classics:2.0.1"    //经典加载
        const val classicHeader = "com.scwang.smart:refresh-header-classics:2.0.1"    //经典刷新头
    }

    object GeTuiPush {
        //个推SDK
        const val sdk = "com.getui:gtsdk:3.1.2.0"  //个推SDK
        const val core = "com.getui:gtc:3.1.0.0"//个推核心组件
    }

    object TingYun {
        const val sdk = "com.networkbench:tingyun-ea-agent-android:2.15.4"
        const val nativeCrash =
            "com.networkbench.newlens.agent.android2:nbs.newlens.nativecrash:2.0.1"
    }

    const val jwt = "com.auth0.android:jwtdecode:2.0.0"
    const val flyTabLayout = "io.github.h07000223:flycoTabLayout:3.0.0"

    object Dokit{
        //滴滴开源的APP工具类集合http://xingyun.xiaojukeji.com/docs/dokit#/androidGuide
        private const val lastversion = "3.4.2.1"
        const val dokitx = "io.github.didi.dokit:dokitx:${lastversion}"
        const val dokitxNoOp ="io.github.didi.dokit:dokitx-no-op:${lastversion}"
    }
}