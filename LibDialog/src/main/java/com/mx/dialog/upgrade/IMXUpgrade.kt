package com.mx.dialog.upgrade

interface IMXUpgrade {
    /**
     * 下载文件
     * @param progress 下载进度更新，当 percent = 1f 时，表示下载成功！
     * @return 返回下载是否成功或失败！
     */
    fun downloadAPK(progress: ((state: IMXDownloadStatus, percent: Float) -> Unit))

    /**
     * 安装下载的文件
     */
    fun installAPK()

    /**
     * 取消当前下载
     */
    fun destroy()
}

enum class IMXDownloadStatus {
    DOWNLOAD, // 下载中
    ERROR, // 下载错误
    SUCCESS // 下载成功
}