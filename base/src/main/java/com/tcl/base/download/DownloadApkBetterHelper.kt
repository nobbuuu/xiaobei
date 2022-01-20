package com.tcl.base.download

import android.content.ContextWrapper
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.rxlife.coroutine.RxLifeScope
import com.tcl.base.utils.TxkToastUtil
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.headersContentLength
import okio.Buffer
import okio.sink
import okio.source
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Author: tiaozi
 * Date : 2021/7/19
 * Drc:
 */
private const val MSG_ID_DOWNLOADING_PREPARE = 1001
private const val MSG_ID_DOWNLOADING_PROGRESS = 1002
private const val MSG_ID_DOWNLOADED = 1003

/**断点下载开关*/
private const val CONTINUE = true

/**初始进度*/
private const val INIT_PROGRESS = 0

object DownloadApkBetterHelper {


    private var config: Config = Config()
    private var downloadJob: Job? = null
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private val okHttpClient: OkHttpClient = okHttpClientBuilder
        .retryOnConnectionFailure(true)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    /**上一次检测版本包长度*/
    private var lastPackageLength: Long = 0L

    /**已下载的包长度*/
    private var downloadedLength: Long = 0L
    private var handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            LogUtils.eTag("mars", "handleMessage>>${msg.what}")
            if (!isCanceled)
                when (msg.what) {
                    MSG_ID_DOWNLOADING_PREPARE -> {
                        config.iupgradeListener?.onUpdate(INIT_PROGRESS)
                    }
                    MSG_ID_DOWNLOADING_PROGRESS -> {
                        config.iupgradeListener?.onUpdate(msg.arg1)
                    }
                    MSG_ID_DOWNLOADED -> {
                        config.iupgradeListener?.onUpdate(100)
                        config.iupgradeListener?.onFinish(getDownloadFile())

                    }
                }
        }
    }
    var scope: RxLifeScope? = null

    //    var isCancel = false
    fun initConfig(
        versionName: String,
        curDownLoadUrl: String,
        curFileName: String,
        iUpgradeListener: IUpgradeListener
    ) {
        LogUtils.eTag("mars", "下载初始化配置：》》》》》》》》")
        LogUtils.eTag("mars", "版本名：$versionName")
        LogUtils.eTag("mars", "下载链接：$curDownLoadUrl")
        LogUtils.eTag("mars", "下载文件名：$curFileName")
        LogUtils.eTag("mars", "下载初始化配置：《《《《《《《")
        config.versionName = versionName
        config.curDownLoadUrl = curDownLoadUrl
        config.curFileName = curFileName
        config.iupgradeListener = iUpgradeListener
    }

    /**
     *  调用入口下载apk
     */
    fun downLoadApk() {
        checkConfig()
        cancel()
        isCanceled = false
        scope = RxLifeScope()
        downloadJob = scope?.launch({
            if (getDownloadFile().exists()) {
                withContext(Dispatchers.IO) {
                    lastPackageLength = MMKV.defaultMMKV()
                        .getLong(getPackageLengthKey(), 0)
                    //获取当前下载文件进度
                    downloadedLength = getDownloadFile().length()
                    //获取在线包大小
                    val response = getApkPackage(isHead = true)
                    val curLength = response.headersContentLength()
                    LogUtils.eTag(
                        "mars",
                        "已下载>>>${getDownloadFile().length()}" + "整包长度>>>$curLength"
                    )
                    if (lastPackageLength == curLength) {
                        if (downloadedLength == curLength) {
                            LogUtils.eTag("mars", "文件存在 >>>当前长度=包长度 直接安装")
                            handler.sendEmptyMessage(MSG_ID_DOWNLOADED)
                        } else if (downloadedLength > curLength) {
                            LogUtils.eTag("mars", "下载文件超过正常包长度 不支持断点>>>开始整包下载>>>>")
                            startDownloadWithDeleteFile()
                        } else {
                            if (CONTINUE) {
                                LogUtils.eTag("mars", "文件存在 >>>当前包长度=包长度 开始断点下载")
                                updateProgress(downloadedLength, curLength)
                                handleResponse(getApkPackage(curLength = downloadedLength))
                            } else {
                                LogUtils.eTag("mars", "文件存在 不支持断点>>>开始整包下载>>>>")
                                startDownloadWithDeleteFile()
                            }
                        }
                    } else {
                        LogUtils.eTag("mars", "文件存在 >>>其他情况 开始整包下载>>>>")
                        startDownloadWithDeleteFile()
                    }
                }
            } else {
                LogUtils.eTag("mars", "文件不存在 >>>开始整包下载>>>>")
                startDownloadWithDeleteFile()
            }
        },
            {
                LogUtils.eTag("mars", "RxLifeScope Error <><><><><><><><><>")
                it.printStackTrace()
                cancel()
                config.iupgradeListener?.onError()
            })
    }

    private fun checkConfig() {
        if (config.curDownLoadUrl == "" || config.curFileName == "") {
            TxkToastUtil.showCentreText("请配置下载路径以及apk文件名")
        }
    }

    var isCanceled = false

    fun cancel() {
        isCanceled = true
        LogUtils.eTag("mars", "取消下载")
        scope?.close()
        downloadJob?.cancelChildren()
        downloadJob?.cancel()
        handler.removeCallbacksAndMessages(null)
    }

    /**重试专用*/
    fun reDownLoadApk() {
        checkConfig()
        config.run {
            LogUtils.eTag("mars", "重试 curDownLoadUrl:$curDownLoadUrl curFileName:$curFileName")
            if (curDownLoadUrl.isNotEmpty() && curFileName.isNotEmpty())
                downLoadApk()
        }
    }

    /**整包下载
     *
     * @param isExist 文件是否存在
     */
    private suspend fun startDownloadWithDeleteFile(isExist: Boolean = true) {
        downloadedLength = 0L
        if (isExist) {
            getDownloadFile().delete()
        }
        updateProgress(0)
        withContext(Dispatchers.IO) {
            savePackageVersionLength(0L)
            handleResponse(getApkPackage(), true)
        }
    }

    /**获取Apk*/
    private fun getApkPackage(isHead: Boolean = false, curLength: Long = 0): Response {
        val request: Request = Request.Builder()
            .apply {
                config.curDownLoadUrl.run {
                    url(this)
                }
                if (isHead) head()
                if (curLength != 0L) header(
                    "Range",
                    String.format(Locale.CHINESE, "bytes=%d-", curLength)
                )
            }.build()
        return okHttpClient.newCall(request).execute()
    }

    /**更新进度条*/
    private fun updateProgress(curLength: Long, packageLength: Long = 100) {
        when {
            curLength == 0L -> {
                handler.sendEmptyMessage(MSG_ID_DOWNLOADING_PREPARE)
            }
            curLength < packageLength -> {
                handler.sendMessage(Message.obtain().apply {
                    what = MSG_ID_DOWNLOADING_PROGRESS
                    arg1 = (curLength * 100 / packageLength).toInt()
                })
            }
            curLength == packageLength -> {
                handler.sendEmptyMessage(MSG_ID_DOWNLOADED)
            }
        }
    }

    private fun handleResponse(response: Response, isFull: Boolean = false) {
        var curLength = 0L
        var isException = false
        try {
            response.body?.run {
                val contentLength = contentLength()
                var curPackageLength = lastPackageLength
                if (isFull) {
                    savePackageVersionLength(contentLength)
                    curPackageLength = contentLength
                }
                LogUtils.eTag("mars", "请求" + (if (isFull) "整" else "余") + "包大小为:$contentLength")
                val inputStream = byteStream()
                val fos = getDownloadFile().sink(CONTINUE)
                val bis = inputStream.source()
                var len: Long
                curLength = downloadedLength
                val buffer = Buffer()
                while (bis.read(buffer, 2048).also { len = it } != -1L) {
                    if (isCanceled) {
                        break
                    }
                    fos.write(buffer, len)
                    curLength += len
                    updateProgress(curLength, curPackageLength)
                }
                fos.close()
                bis.close()
                inputStream.close()
            }
        } catch (e: Exception) {
            isException = true
        } finally {
            if (isException) {
                config.iupgradeListener?.onError()
                cancel()
            }
            LogUtils.eTag("mars", "下载" + (if (isException) "异常" else "正常") + "结束当前进度>>>$curLength")
        }
    }

    /**获取下载文件的地址*/
    @Throws(Exception::class)
    fun getDownloadFile(): File {
        checkConfig()
        val cw = ContextWrapper(ActivityUtils.getTopActivity().application)
        return File(
            cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            config.curFileName
        )
    }

    /**保存对应包版本以及长度*/
    private fun savePackageVersionLength(length: Long) {
        LogUtils.eTag("mars", "当前${getPackageLengthKey()}，长度 $length")
        MMKV.defaultMMKV().encode(getPackageLengthKey(), length)
    }

    private fun getPackageLengthKey() = config.versionName + KEY_PACKAGE_LENGTH
    class Config {
        var versionName: String = ""
        var iupgradeListener: IUpgradeListener? = null
        var curFileName: String = ""
        var curDownLoadUrl: String = ""
    }


}


interface IUpgradeListener {
    fun onUpdate(progress: Int)
    fun onError()
    fun onFinish(file: File)
}