package com.mx.dialog.base

import android.content.Context
import android.graphics.RectF
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mx.dialog.R
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.utils.MXDrawableUtils
import com.mx.dialog.utils.MXDialogUtils
import kotlin.math.min

/**
 * 集成内容定位的功能
 * 详情见：setPosition()
 */
open class MXBaseCardDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseDialog(context, fullScreen) {
    private var dialogBackgroundColor: Int? = null
    private var cardBackgroundRadiusDP: Float? = 10f
    private var cardMarginDP: RectF? = RectF(25f, 25f, 25f, 25f)
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

        val cardRadiusDP = cardBackgroundRadiusDP
        if (cardRadiusDP != null && cardRadiusDP > 0) {
            mxCardLay?.background = MXDrawableUtils.buildGradientDrawable(
                context, cardRadiusDP,
                context.resources.getColor(R.color.mx_dialog_color_background)
            )
        } else {
            mxCardLay?.setBackgroundResource(R.drawable.mx_dialog_card_bg)
        }

        kotlin.run { // 位置设置
            val marginLeft = MXDialogUtils.dp2px(context, cardMarginDP?.left ?: 0f)
            val marginTop = MXDialogUtils.dp2px(context, cardMarginDP?.top ?: 0)
            val marginRight = MXDialogUtils.dp2px(context, cardMarginDP?.right ?: 0)
            val marginBottom = MXDialogUtils.dp2px(context, cardMarginDP?.bottom ?: 0)

            val lp = (mxCardLay?.layoutParams as FrameLayout.LayoutParams?)
            lp?.gravity = position.gravity
            mxCardLay?.layoutParams = lp

            mxRootLay?.setPadding(marginLeft, marginTop, marginRight, marginBottom)

            mxCardLay?.translationX =
                MXDialogUtils.dp2px(context, position.translationX ?: 0).toFloat()
            mxCardLay?.translationY =
                MXDialogUtils.dp2px(context, position.translationY ?: 0).toFloat()
        }

        kotlin.run {
            val color = dialogBackgroundColor
                ?: context.resources.getColor(R.color.mx_dialog_color_background_alpha)
            mxRootLay?.setBackgroundColor(color)
        }
    }

    /**
     * 设置弹窗背景颜色
     */
    fun setDialogBackGroundColor(color: Int) {
        dialogBackgroundColor = color

        initCard()
    }

    /**
     * 设置内容ViewGroup 背景圆角半径
     */
    fun setCardBackgroundRadius(radiusDP: Float) {
        cardBackgroundRadiusDP = radiusDP

        initCard()
    }

    /**
     * 设置弹窗位置
     */
    fun setCardPosition(position: MXDialogPosition) {
        this.position = position

        initCard()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     */
    fun setCardMargin(margin: Float) {
        cardMarginDP = RectF(margin, margin, margin, margin)

        initCard()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     */
    fun setCardMargin(horizontal: Float, vertical: Float) {
        cardMarginDP = RectF(horizontal, vertical, horizontal, vertical)

        initCard()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     */
    fun setCardMargin(left: Float, top: Float, right: Float, bottom: Float) {
        cardMarginDP = RectF(left, top, right, bottom)

        initCard()
    }
}