package com.mx.dialog.progress

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.TextView
import com.mx.dialog.MXBaseDialog
import com.mx.dialog.R

class MXLoadingDialog(context: Context) : MXBaseDialog(context) {
    private var loadingMessage: CharSequence? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private var dismissDelayTime: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_loading)
        initView()
        initData()
    }

    private fun initView() {
        findViewById<ViewGroup>(R.id.rootLay)?.setOnClickListener {
            onBackPressed()
        }
        findViewById<ViewGroup>(R.id.cardLay)?.setOnClickListener { }
        findViewById<TextView>(R.id.loadingTxv)?.text = loadingMessage ?: "正在加载中..."
    }

    private fun initData() {
        mHandler.removeCallbacksAndMessages(null)
        val delay = dismissDelayTime ?: return
        mHandler.postDelayed({
            if (isShowing) {
                dismiss()
            }
        }, delay * 1000L)
    }

    fun setDismissDelay(delay: Int) {
        dismissDelayTime = delay

        initData()
    }

    fun setMessage(message: CharSequence) {
        loadingMessage = message

        initData()
    }
}