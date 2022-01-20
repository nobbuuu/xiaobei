/**
 * @author tiaozi
 * @date   2021/12/08
 * description
 */
@Suppress("SpellCheckingInspection", "unused")
object AndroidX {

    const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha01"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha03"
    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val activityKtx = "androidx.activity:activity-ktx:1.3.0-alpha03"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val paging = "androidx.paging:paging-runtime-ktx:2.1.2"
    const val viewpager = "androidx.viewpager:viewpager:1.0.0"
    const val exifinterface = "androidx.exifinterface:exifinterface:1.3.2"
    const val annotationVer = "androidx.annotation:annotation:1.2.0-beta01"

    val fragment = Fragment
    val lifecyle = Lifecycle
    val navigation = Navigation
    val room = Room

    object Fragment {
        private const val fragment_version = "1.3.3"
        const val fragment = "androidx.fragment:fragment:$fragment_version"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:$fragment_version"
        const val fragmentTesting = "androidx.fragment:fragment-testing:$fragment_version"
    }

    object Lifecycle {
        private const val lifecycle_version = "2.2.0"
        const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
        const val lifecycle_ext = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val viewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
        const val service = "androidx.lifecycle:lifecycle-service:$lifecycle_version"
    }

    object Navigation {
        private const val navigation_version = "2.3.2"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$navigation_version"
    }

    object Room {
        private const val room_version = "2.2.5"
        const val roomRuntime = "androidx.room:room-runtime:$room_version"
        const val roomCompiler = "androidx.room:room-compiler:$room_version"
        const val roomKtx = "androidx.room:room-ktx:$room_version"
    }
}