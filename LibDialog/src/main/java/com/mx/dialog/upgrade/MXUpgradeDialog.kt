package com.mx.dialog.upgrade

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog
import com.mx.dialog.utils.MXUtils.asString
import com.mx.dialog.views.MXRatioFrameLayout
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.roundToInt

open class MXUpgradeDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseCardDialog(context, fullScreen) {
    private val mHandler = Handler(Looper.getMainLooper())
    private var closeBtn: ImageView? = null
    private var titleTxv: TextView? = null
    private var contentLay: MXRatioFrameLayout? = null
    private var msgTxv: TextView? = null
    private var progressBar: ProgressBar? = null
    private var actionBtn: TextView? = null

    private var titleStr: CharSequence? = null
    private var msgStr: CharSequence? = null

    private var iUpgrade: IMXUpgrade? = null
    private var autoInstallWhenDownload = true

    override fun getContentLayoutId(): Int {
        return R.layout.mx_dialog_upgrade
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        closeBtn = findViewById(R.id.closeBtn)
        titleTxv = findViewById(R.id.titleTxv)
        contentLay = findViewById(R.id.contentLay)
        msgTxv = findViewById(R.id.msgTxv)
        progressBar = findViewById(R.id.progressBar)
        actionBtn = findViewById(R.id.actionBtn)

        contentLay?.setMaxHeightRatio(0.5f)
        processInitStage()
    }

    override fun initDialog() {
        super.initDialog()
        if (isCancelable()) {
            closeBtn?.visibility = View.VISIBLE
            closeBtn?.setOnClickListener {
                dismiss()
            }
        } else {
            closeBtn?.visibility = View.GONE
        }

        titleTxv?.text = titleStr ?: context.resources.getString(R.string.mx_dialog_upgrade_title)
        msgTxv?.text = msgStr
    }

    override fun setTitle(title: CharSequence?) {
        titleStr = title
        initDialog()
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.getString(titleId)
        initDialog()
    }

    fun setMessage(message: CharSequence?) {
        msgStr = message
        initDialog()
    }

    /**
     * 设置升级回调相关
     */
    fun setIUpgrade(upgrade: IMXUpgrade?) {
        iUpgrade = upgrade
    }

    /**
     * 设置下载完成后自动安装
     */
    fun setAutoInstall(autoInstall: Boolean) {
        autoInstallWhenDownload = autoInstall
    }

    private val downloadClickListener = View.OnClickListener {
        progressBar?.visibility = View.VISIBLE
        progressBar?.max = 1000
        progressBar?.progress = 0
        actionBtn?.isEnabled = false
        actionBtn?.text = "下载中 0.00%"
        thread {
            try {
                var lastUpdateTime = 0L
                iUpgrade?.downloadAPK { state, percent ->
                    if (!isShowing) return@downloadAPK
                    if (state == IMXDownloadStatus.DOWNLOAD
                        && abs(lastUpdateTime - System.currentTimeMillis()) < 200
                        && percent < 1f
                    ) {
                        return@downloadAPK
                    }
                    lastUpdateTime = System.currentTimeMillis()
                    mHandler.post {
                        if (!isShowing) return@post
                        when (state) {
                            IMXDownloadStatus.DOWNLOAD -> {
                                actionBtn?.text = "下载中 ${(percent * 100f).asString()}%"
                                progressBar?.progress = (percent * 1000).roundToInt()
                            }
                            IMXDownloadStatus.ERROR -> {
                                processErrorStage("下载失败")
                            }
                            IMXDownloadStatus.SUCCESS -> {
                                processInstallStage()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val installClickListener = View.OnClickListener {
        try {
            iUpgrade?.installAPK()
        } catch (e: Exception) {
            e.printStackTrace()
            processErrorStage("安装失败")
        }
    }

    private fun processInitStage() {
        actionBtn?.text = context.getString(R.string.mx_dialog_upgrade_download)
        progressBar?.visibility = View.INVISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(downloadClickListener)
    }

    private fun processInstallStage() {
        actionBtn?.text = context.getString(R.string.mx_dialog_upgrade_install)
        progressBar?.visibility = View.INVISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(installClickListener)

        if (autoInstallWhenDownload) {
            try {
                iUpgrade?.installAPK()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun processErrorStage(message: String) {
        actionBtn?.text = "$message，重新下载"
        progressBar?.visibility = View.INVISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(downloadClickListener)
    }

    override fun dismiss() {
        iUpgrade?.destroy()
        super.dismiss()
    }
}