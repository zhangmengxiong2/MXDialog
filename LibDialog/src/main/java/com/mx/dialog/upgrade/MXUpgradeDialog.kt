package com.mx.dialog.upgrade

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog
import java.io.File
import java.lang.Exception
import kotlin.concurrent.thread

open class MXUpgradeDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseCardDialog(context, fullScreen) {
    private val mHandler = Handler(Looper.getMainLooper())
    private var titleTxv: TextView? = null
    private var msgTxv: TextView? = null
    private var actionBtn: TextView? = null

    private var titleStr: CharSequence? = null
    private var msgStr: CharSequence? = null

    private var downloadUrl: String? = null
    private var downloadFile: File? = null

    private var iUpgrade: IMXUpgrade? = null

    override fun getContentLayoutId(): Int {
        return R.layout.mx_dialog_upgrade
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCardMargin(40f)
        titleTxv = findViewById(R.id.titleTxv)
        msgTxv = findViewById(R.id.msgTxv)
        actionBtn = findViewById(R.id.actionBtn)

        processInitStage()
    }

    override fun initDialog() {
        super.initDialog()
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

    fun setIUpgrade(url: String, upgrade: IMXUpgrade?) {
        downloadUrl = url
        iUpgrade = upgrade
    }

    private val downloadClickListener = View.OnClickListener {
        val url = downloadUrl ?: return@OnClickListener
        actionBtn?.visibility = View.VISIBLE
        actionBtn?.isEnabled = false
        actionBtn?.text = "下载中 0%"
        var percent = 0
        thread {
            val result = try {
                iUpgrade?.download(this, url) { p ->
                    if (p != percent) {
                        mHandler.post { actionBtn?.text = "下载中 ${p}%" }
                        percent = p
                    }
                }!!
            } catch (e: Exception) {
                e.printStackTrace()
                mHandler.post { processErrorStage("下载失败") }
                return@thread
            }
            mHandler.post {
                downloadFile = result
                processInstallStage()
            }
        }
    }

    private val installClickListener = View.OnClickListener {
        val file = downloadFile ?: return@OnClickListener
        if (!file.exists()) return@OnClickListener
        try {
            iUpgrade?.install(context, file)
        } catch (e: Exception) {
            e.printStackTrace()
            processErrorStage("安装失败")
        }
    }

    private fun processInitStage() {
        actionBtn?.text = "下载安装"
        actionBtn?.visibility = View.VISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(downloadClickListener)
    }

    private fun processInstallStage() {
        actionBtn?.text = "下载完成，去安装"
        actionBtn?.visibility = View.VISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(installClickListener)
    }

    private fun processErrorStage(message: String) {
        actionBtn?.text = "$message，重新下载"
        actionBtn?.visibility = View.VISIBLE
        actionBtn?.isEnabled = true
        actionBtn?.setOnClickListener(downloadClickListener)
    }
}