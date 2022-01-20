@file:Suppress("unused")

package com.tcl.base.download

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import java.io.File

private const val TAG = "InstallApkHelper"
const val REQUEST_INSTALL = 0x100123

class InstallApkHelper(
    private val apkPath: String,
    private val activity: Activity = ActivityUtils.getTopActivity() //下载下来后文件的路径
) {
    fun installApk() {
        if (TextUtils.isEmpty(apkPath)) {
            return
        }
        val file = File(apkPath)
        val intent = Intent(Intent.ACTION_VIEW)
//        LogUtils.i(TAG, "file:$file")
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //provider authorities
//            Uri apkUri = FileProvider.getUriForFile(context, "你的包名", file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val authroty = "com.tcl.tsales_android" + ".fileprovider" //BuildConfig.APPLICATION_ID
            val contentUri = FileProvider.getUriForFile(
                activity,
                authroty, file
            )
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        activity.startActivity(intent)
    }


    fun install(context: Context?, apkPath: String) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return
        }
        val file = File(apkPath)
        val intent = Intent(Intent.ACTION_VIEW)

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            LogUtils.v(TAG, "7.0以上，正在安装apk...")
            //provider authorities
//            Uri apkUri = FileProvider.getUriForFile(context, "com.tcl.tsales_android.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            LogUtils.v(TAG, "7.0以下，正在安装apk...")
        }
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    fun install() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> startInstallO()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> startInstallN()
            else -> startInstall()
        }
    }

    /**
     * android1.x-6.x
     */
    private fun startInstall() {
        val install = Intent(Intent.ACTION_VIEW)
        install.setDataAndType(
            Uri.parse("file://$apkPath"),
            "application/vnd.android.package-archive"
        )
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(install)
    }

    /**
     * android7.x
     */
    private fun startInstallN() {
        //参数1 上下文, 参数2 在AndroidManifest中的android:authorities值, 参数3  共享的文件
        val apkUri = FileProvider.getUriForFile(
            activity, getAuthority(
                activity, ".fileprovider"
            ), File(apkPath)
        )
        val install = Intent(Intent.ACTION_VIEW)
        //由于没有在Activity环境下启动Activity,设置下面的标签
        install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        install.setDataAndType(apkUri, "application/vnd.android.package-archive")
        activity.startActivity(install)
    }

    /**
     * android8.x
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallO() {
        val isGranted = activity.packageManager.canRequestPackageInstalls()
        if (isGranted) startInstallN() //安装应用的逻辑(写自己的就可以)
        else AlertDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("安装应用需要打开未知来源权限，请去设置中开启权限")
            .setPositiveButton("确定") { _, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                activity.startActivityForResult(intent, UNKNOWN_CODE)
            }
            .show()
    }

    /**
     * 获取FileProvider
     * 返回： "此处为你的包名.FileProvider"
     * china.test.provider
     */
    @Suppress("SameParameterValue")
    private fun getAuthority(context: Context, authority: String): String {
        return getAppProcessName(context) + authority
    }

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    private fun getAppProcessName(context: Context): String {
        //当前应用pid
        val pid = Process.myPid()
        //任务管理类
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //遍历所有应用
        val infos = manager.runningAppProcesses
        for (info in infos) {
            if (info.pid == pid) //得到当前应用
                return info.processName //返回包名
        }
        return ""
    }

    companion object {
        var UNKNOWN_CODE = 2019
    }
}