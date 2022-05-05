package com.mx.example

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 网络连接帮助类
 *
 * @author zmx
 */
object HttpDataHelp {
    private val JSON = "Content-Type=application/json;charset=UTF-8".toMediaTypeOrNull()

    // 网络连接部分
    private const val CHARSET_NAME = "UTF-8"
    private const val TIME_OUT = 360L
    private const val CACHE_DEFAULT = 30

    private val CLIENT: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        builder.build()
    }

    fun download(url: String?, target: File, listener: ((Int) -> Unit)? = null): Boolean? {
        if (url.isNullOrEmpty()) return false
        if (target.exists()) {
            target.delete()
        }
        target.createNewFile()

        var hasDownload: Boolean? = null
        val request = Request.Builder()
            .url(url)
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()
        try {
            CLIENT.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    hasDownload = true
                }

                override fun onResponse(call: Call, response: Response) {
                    val inputStream = response.body?.source()?.inputStream() ?: return

                    val bufferedOutputStream = BufferedOutputStream(FileOutputStream(target))
                    val data = ByteArray(1024 * 1024)
                    var len = 0
                    var read_size = 0
                    var percent = 0
                    val available = response.body?.contentLength() ?: -1
                    while (inputStream.read(data).also { len = it } >= 0) {
                        bufferedOutputStream.write(data, 0, len)
                        read_size += len
                        val p = ((read_size * 100f) / available).toInt()
                        if (p != percent) {
                            listener?.invoke(p)
                            percent = p
                        }
                    }
                    bufferedOutputStream.flush()
                    bufferedOutputStream.close()
                    inputStream.close()
                    hasDownload = true
                }
            })
            while (hasDownload == null) {
                Thread.sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
        }
        return hasDownload
    }
}
