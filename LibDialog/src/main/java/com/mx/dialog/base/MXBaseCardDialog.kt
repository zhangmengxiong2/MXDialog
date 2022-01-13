package com.mx.dialog.base

import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mx.dialog.R
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.utils.MXDialogUtils
import com.mx.dialog.utils.MXDrawableUtils

/**
 * 集成内容定位的功能
 * 详情见：setPosition()
 */
abstract class MXBaseCardDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseDialog(context, fullScreen) {
    private var closeOnTouchOutside: Boolean = true
    private var dialogBackgroundColor: Int? = null
    private var cardBackgroundRadiusDP = 10f
    private var cardMarginDP = RectF(25f, 25f, 25f, 25f)
    private var position = MXDialogPosition.CENTER
    private var mxRootLay: ViewGroup? = null
    private var mxCardLay: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_base_card)
        initView()
    }

    private fun initView() {
        if (mxRootLay == null) mxRootLay = findViewById(R.id.mxRootLay)
        if (mxCardLay == null) mxCardLay = findViewById(R.id.mxCardLay)

        val content = LayoutInflater.from(context).inflate(getContentLayoutId(), mxCardLay, false)
        mxCardLay?.removeAllViews()
        mxCardLay?.addView(
            content,
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }


    abstract fun getContentLayoutId(): Int

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    /**
     * 点击空白处时会不会触发消失响应
     */
    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        closeOnTouchOutside = cancel
        super.setCanceledOnTouchOutside(cancel)
    }

    protected open fun initDialog() {
        mxRootLay?.setOnClickListener {
            if (closeOnTouchOutside) {
                onBackPressed()
            }
        }
        mxCardLay?.setOnClickListener { }

        if (cardBackgroundRadiusDP > 0) {
            mxCardLay?.background = MXDrawableUtils.buildGradientDrawable(
                context, context.resources.getColor(R.color.mx_dialog_color_background),
                cardBackgroundRadiusDP
            )
        } else {
            mxCardLay?.setBackgroundResource(R.drawable.mx_dialog_card_bg)
        }

        kotlin.run { // 位置设置
            val marginLeft = MXDialogUtils.dp2px(context, cardMarginDP.left)
            val marginTop = MXDialogUtils.dp2px(context, cardMarginDP.top)
            val marginRight = MXDialogUtils.dp2px(context, cardMarginDP.right)
            val marginBottom = MXDialogUtils.dp2px(context, cardMarginDP.bottom)

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

        initDialog()
    }

    /**
     * 设置内容ViewGroup 背景圆角半径
     */
    fun setCardBackgroundRadius(radiusDP: Float) {
        cardBackgroundRadiusDP = radiusDP

        initDialog()
    }

    fun getCardBackgroundRadiusDP() = cardBackgroundRadiusDP

    /**
     * 设置弹窗位置
     */
    fun setCardPosition(position: MXDialogPosition) {
        this.position = position

        initDialog()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     */
    fun setCardMargin(margin: Float) {
        cardMarginDP = RectF(margin, margin, margin, margin)

        initDialog()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     */
    fun setCardMargin(horizontal: Float, vertical: Float) {
        cardMarginDP = RectF(horizontal, vertical, horizontal, vertical)

        initDialog()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     */
    fun setCardMargin(left: Float, top: Float, right: Float, bottom: Float) {
        cardMarginDP = RectF(left, top, right, bottom)

        initDialog()
    }
}