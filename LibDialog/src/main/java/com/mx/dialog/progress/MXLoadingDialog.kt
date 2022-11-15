package com.mx.dialog.progress

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog
import com.mx.dialog.utils.MXUtils

open class MXLoadingDialog(context: Context) : MXBaseCardDialog(context) {
    private var loadingMessage: CharSequence? = null
    private var indeterminateDrawable: Drawable? = null

    private var loadingImg: ImageView? = null
    private var loadingTxv: TextView? = null

    private var loadingAnimator: ObjectAnimator? = null
    private var userSetAnimator: ObjectAnimator? = null

    override fun getContentLayoutId(): Int {
        return R.layout.mx_content_loading
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initCardView(cardLay: ViewGroup) {
        super.initCardView(cardLay)
        val lp = cardLay.layoutParams as FrameLayout.LayoutParams
        lp.width = FrameLayout.LayoutParams.WRAP_CONTENT
        cardLay.layoutParams = lp
    }

    private fun initView() {
        loadingImg = findViewById(R.id.mxLoadingImg)
        loadingTxv = findViewById(R.id.mxLoadingTxv)

        loadingImg?.let { loadingAnimator = MXUtils.rotationAnimation(it, 1000) }
    }

    override fun show() {
        super.show()
        initData()
    }

    private fun initData() {
        loadingTxv?.text = loadingMessage ?: "正在加载中..."
        loadingImg?.let { imageView -> // Icon设置
            val drawable = indeterminateDrawable
            if (drawable != null) {
                imageView.setImageDrawable(drawable)
                userSetAnimator?.start()
            } else {
                imageView.setImageResource(R.drawable.mx_dialog_icon_loading)
                loadingAnimator?.start()
            }
        }
    }

    fun setMessage(message: CharSequence) {
        loadingMessage = message

        initData()
    }

    override fun onDismissTicket(maxSecond: Int, remindSecond: Int) {
    }

    fun setIndeterminateDrawable(drawable: Drawable, animator: ObjectAnimator? = null) {
        indeterminateDrawable = drawable
        this.loadingAnimator?.end()
        this.userSetAnimator?.end()
        this.userSetAnimator = animator

        initData()
    }

    override fun dismiss() {
        loadingAnimator?.cancel()
        userSetAnimator?.cancel()

        super.dismiss()
    }
}