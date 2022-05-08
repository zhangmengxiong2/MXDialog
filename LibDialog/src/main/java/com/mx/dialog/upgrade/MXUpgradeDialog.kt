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
import kotlin.concurrent.thread

open class MXUpgradeDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseCardDialog(context, fullScreen) {
    private val mHandler = Handler(Looper.getMainLooper())
    private var closeBtn: ImageView? = null
    private var titleTxv: TextView? = null
    private var msgTxv: TextView? = null
    private var progressBar: ProgressBar? = null
    private var actionBtn: TextView? = null

    private var titleStr: CharSequence? = null
    private var msgStr: CharSequence? = null

    private var iUpgrade: IMXUpgrade? = null

    override fun getContentLayoutId(): Int {
        return R.layout.mx_dialog_upgrade
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCardMargin(40f)
        setCardBackgroundRadius(16f)
        closeBtn = findViewById(R.id.closeBtn)
        titleTxv = findViewById(R.id.titleTxv)
        msgTxv = findViewById(R.id.msgTxv)
        progressBar = findViewById(R.id.progressBar)
        actionBtn = findViewById(R.id.actionBtn)
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

    fun setIUpgrade(upgrade: IMXUpgrade?) {
        iUpgrade = upgrade
    }

    private val downloadClickListener = View.OnClickListener {
        progressBar?.visibility = View.VISIBLE
        progressBar?.max = 1000
        progressBar?.progress = 0
        actionBtn?.isEnabled = false
        actionBtn?.text = "下载中 0%"
        thread {
            try {
                iUpgrade?.downloadAPK { state, percent ->
                    mHandler.post {
                        if (!isShowing) return@post
                        when (state) {
                            IMXDownloadStatus.DOWNLOAD -> {
                                actionBtn?.text = "下载中 ${(percent * 100f).asString()}%"
                                progressBar?.progress = (percent * 1000).toInt()
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
        actionBtn?.text = "下载安装"
        progressBar?.visibility = View.INVISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(downloadClickListener)
    }

    private fun processInstallStage() {
        actionBtn?.text = "下载完成，去安装"
        progressBar?.visibility = View.INVISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(installClickListener)
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