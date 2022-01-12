package com.mx.dialog.progress

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog

open class MXLoadingDialog(context: Context) : MXBaseCardDialog(context) {
    private var loadingMessage: CharSequence? = null
    private var indeterminateDrawable: Drawable? = null

    private var progressBar: ProgressBar? = null
    private var loadingTxv: TextView? = null

    override fun getContentLayoutId(): Int {
        return R.layout.mx_content_loading
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        progressBar = findViewById(R.id.progressBar)
        loadingTxv = findViewById(R.id.loadingTxv)
    }

    private fun initData() {
        loadingTxv?.text = loadingMessage ?: "正在加载中..."

        kotlin.run { // Icon设置
            val width =
                context.resources.getDimensionPixelOffset(R.dimen.mx_dialog_progress_img_size)
            val drawable = indeterminateDrawable
                ?: context.resources.getDrawable(R.drawable.mx_dialog_progress_loading)
            drawable.setBounds(0, 0, width, width)
            progressBar?.indeterminateDrawable = drawable
        }
    }

    fun setMessage(message: CharSequence) {
        loadingMessage = message

        initData()
    }

    override fun onDismissTicket(maxSecond: Int, remindSecond: Int) {

    }

    fun setIndeterminateDrawable(drawable: Drawable) {
        indeterminateDrawable = drawable

        initData()
    }
}