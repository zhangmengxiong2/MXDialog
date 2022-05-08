package com.mx.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.mx.dialog.upgrade.IMXDownloadStatus
import com.mx.dialog.upgrade.IMXUpgrade
import okhttp3.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class MXUpgradeImp(private val url: String, private val context: Context) : IMXUpgrade {
    private var currentCall: Call? = null
    private var isActive = true
    private var targetFile: File? = null
    override fun downloadAPK(progress: (state: IMXDownloadStatus, percent: Float) -> Unit) {
        currentCall?.cancel()
        isActive = true
        targetFile?.delete()
        targetFile = null

        val file = File(
            context.cacheDir,
            "${context.packageName}_${System.currentTimeMillis()}.apk"
        )
        file.createNewFile()
        val request = Request.Builder()
            .url(url)
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()
        try {
            val call = OkHttpClient.Builder()
                .connectTimeout(60 * 2, TimeUnit.SECONDS)
                .writeTimeout(60 * 2, TimeUnit.SECONDS)
                .readTimeout(60 * 2, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build().newCall(request)
            currentCall = call
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    file.delete()
                    progress.invoke(
                        IMXDownloadStatus.ERROR,
                        0f
                    )
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val inputStream = response.body?.source()?.inputStream() ?: return
                        val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
                        val data = ByteArray(1024 * 1024 * 10)
                        var len = 0
                        var read_size = 0
                        val available = response.body?.contentLength() ?: -1
                        while (isActive && inputStream.read(data).also { len = it } >= 0) {
                            bufferedOutputStream.write(data, 0, len)
                            read_size += len
                            progress.invoke(
                                IMXDownloadStatus.DOWNLOAD,
                                read_size.toFloat() / available
                            )
                        }
                        bufferedOutputStream.flush()
                        bufferedOutputStream.close()
                        inputStream.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        file.delete()
                        progress.invoke(
                            IMXDownloadStatus.ERROR,
                            0f
                        )
                        return
                    }
                    targetFile = file
                    progress.invoke(
                        IMXDownloadStatus.SUCCESS,
                        1f
                    )
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            file.delete()
            progress.invoke(
                IMXDownloadStatus.ERROR,
                0f
            )
        }
    }

    override fun installAPK() {
        val file = targetFile
        if (file == null || !file.exists()) {
            throw Exception("安装文件错误！")
        }
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT >= 24) {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileProvider",
                file
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        context.startActivity(intent)
    }

    override fun destroy() {
        isActive = false
        currentCall?.cancel()
        currentCall = null
        targetFile = null
    }
}