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
    private var position = MXDialogPosition.CENTER
    private var rootLay: ViewGroup? = null
    private var cardLay: ViewGroup? = null

    override fun onStart() {
        super.onStart()
        initCard()
    }

    private fun initCard() {
        if (rootLay == null) rootLay = findViewById(R.id.rootLay)
        if (cardLay == null) cardLay = findViewById(R.id.cardLay)
        rootLay?.setOnClickListener {
            onBackPressed()
        }
        cardLay?.setOnClickListener { }

        kotlin.run { // 位置设置
            val lp = (cardLay?.layoutParams as FrameLayout.LayoutParams?)
            lp?.gravity = position.gravity
            cardLay?.layoutParams = lp

            rootLay?.setPadding(
                0,
                MXDialogUtils.getStatusBarHeight(context) + MXDialogUtils.dp2px(
                    context,
                    position.marginTop ?: 0
                ),
                0,
                MXDialogUtils.getNavigationBarHeight(context) + MXDialogUtils.dp2px(
                    context,
                    position.marginBottom ?: 0
                )
            )

            cardLay?.translationX =
                MXDialogUtils.dp2px(context, position.translationX ?: 0).toFloat()
            cardLay?.translationY =
                MXDialogUtils.dp2px(context, position.translationY ?: 0).toFloat()
        }
    }

    fun setPosition(position: MXDialogPosition) {
        this.position = position

        initCard()
    }
}