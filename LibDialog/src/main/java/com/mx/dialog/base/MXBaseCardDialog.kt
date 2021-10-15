package com.mx.dialog.base

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mx.dialog.R
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.utils.MXDialogUtils

/**
 * 集成内容定位的功能
 * 详情见：setPosition()
 */
open class MXBaseCardDialog(context: Context) : MXBaseDialog(context) {
    private var backgroundColor: Int? = null
    private var position = MXDialogPosition.CENTER
    private var mxRootLay: ViewGroup? = null
    private var mxCardLay: ViewGroup? = null

    override fun onStart() {
        super.onStart()
        initCard()
    }

    private fun initCard() {
        if (mxRootLay == null) mxRootLay = findViewById(R.id.mxRootLay)
        if (mxCardLay == null) mxCardLay = findViewById(R.id.mxCardLay)
        mxRootLay?.setOnClickListener {
            onBackPressed()
        }
        mxCardLay?.setOnClickListener { }

        kotlin.run { // 位置设置
            val lp = (mxCardLay?.layoutParams as FrameLayout.LayoutParams?)
            lp?.gravity = position.gravity
            mxCardLay?.layoutParams = lp

            mxRootLay?.setPadding(
                0,
                MXDialogUtils.dp2px(
                    context,
                    position.marginTop ?: 0
                ),
                0,
                MXDialogUtils.dp2px(
                    context,
                    position.marginBottom ?: 0
                )
            )

            mxCardLay?.translationX =
                MXDialogUtils.dp2px(context, position.translationX ?: 0).toFloat()
            mxCardLay?.translationY =
                MXDialogUtils.dp2px(context, position.translationY ?: 0).toFloat()
        }

        kotlin.run {
            val color = backgroundColor
                ?: context.resources.getColor(R.color.mx_dialog_color_background_alpha)
            mxRootLay?.setBackgroundColor(color)
        }
    }

    fun setBackGroundColor(color: Int) {
        backgroundColor = color

        initCard()
    }

    fun setPosition(position: MXDialogPosition) {
        this.position = position

        initCard()
    }
}