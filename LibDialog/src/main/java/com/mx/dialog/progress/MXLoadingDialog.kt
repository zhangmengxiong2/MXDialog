package com.mx.dialog.progress

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseDialog

open class MXLoadingDialog(context: Context) : MXBaseDialog(context) {
    private var loadingMessage: CharSequence? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private var dismissDelayTime: Int? = null
    private var indeterminateDrawable: Drawable? = null

    private var rootLay: ViewGroup? = null
    private var cardLay: ViewGroup? = null
    private var progressBar: ProgressBar? = null
    private var loadingTxv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_loading)
        initView()
        initData()
    }

    private fun initView() {
        rootLay = findViewById(R.id.rootLay)
        cardLay = findViewById(R.id.cardLay)
        progressBar = findViewById(R.id.progressBar)
        loadingTxv = findViewById(R.id.loadingTxv)
    }

    private fun initData() {
        if (rootLay == null) return

        rootLay?.setOnClickListener {
            onBackPressed()
        }
        cardLay?.setOnClickListener { }
        loadingTxv?.text = loadingMessage ?: "正在加载中..."

        val width = context.resources.getDimensionPixelOffset(R.dimen.mx_dialog_progress_img_size)
        val drawable = indeterminateDrawable
            ?: context.resources.getDrawable(R.drawable.mx_dialog_progress_loading)
        drawable.setBounds(0, 0, width, width)
        progressBar?.indeterminateDrawable = drawable

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

    fun setIndeterminateDrawable(drawable: Drawable) {
        indeterminateDrawable = drawable

        initData()
    }
}