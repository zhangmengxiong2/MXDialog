package com.mx.dialog.list

import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.utils.MXDialogUtils
import com.mx.dialog.utils.MXDrawableUtils
import com.mx.dialog.utils.MXTextProp
import com.mx.dialog.views.MXRatioFrameLayout

open class MXBaseListDialog(context: Context, fullScreen: Boolean) :
    MXBaseDialog(context, fullScreen) {
    private var includeStatusBarHeight: Boolean = false
    private var includeNavigationBarHeight: Boolean = false

    private var titleStr: CharSequence? = null

    private var dialogBackgroundColor: Int? = null
    private var contentCornerRadiusDP = 12f
    private var contentMaxHeightRatioDP = 0.8f
    private var cardMarginDP = RectF(22f, 22f, 22f, 22f)
    private var position = MXDialogPosition.BOTTOM
    private var mxRootLay: LinearLayout? = null
    private var mxCardLay: ViewGroup? = null
    private var contentLay: MXRatioFrameLayout? = null
    private var btnDivider: View? = null
    private var cancelBtn: TextView? = null
    private var okBtn: TextView? = null
    private var titleTxv: TextView? = null
    private var titleLay: LinearLayout? = null
    private var listView: ListView? = null

    private val cancelProp = MXTextProp()
    private val actionProp = MXTextProp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_list)
        initView()
    }

    private fun initView() {
        if (mxRootLay == null) mxRootLay = findViewById(R.id.mxRootLay)
        if (mxCardLay == null) mxCardLay = findViewById(R.id.mxCardLay)
        if (contentLay == null) contentLay = findViewById(R.id.contentLay)
        if (btnDivider == null) btnDivider = findViewById(R.id.btnDivider)
        if (cancelBtn == null) cancelBtn = findViewById(R.id.cancelBtn)
        if (okBtn == null) okBtn = findViewById(R.id.okBtn)
        if (titleTxv == null) titleTxv = findViewById(R.id.titleTxv)
        if (titleLay == null) titleLay = findViewById(R.id.titleLay)
        if (listView == null) listView = findViewById(R.id.listView)
    }

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    protected open fun initDialog() {
        if (listView == null) return

        contentLay?.setMaxHeightRatio(contentMaxHeightRatioDP)

        if (titleStr != null) {
            titleTxv?.text = titleStr
            titleLay?.visibility = View.VISIBLE
        } else {
            titleLay?.visibility = View.GONE
        }

        mxRootLay?.setOnClickListener {
            if (isCanceledOnTouchOutside()) {
                onBackPressed()
            }
        }
        mxCardLay?.setOnClickListener { }

        kotlin.run {
            processCancelBtn()
            processActionBtn()
            if (cancelBtn?.visibility == View.VISIBLE && okBtn?.visibility == View.VISIBLE) {
                btnDivider?.visibility = View.VISIBLE
            } else {
                btnDivider?.visibility = View.GONE
            }
        }

        if (contentCornerRadiusDP >= 0) {
            mxCardLay?.background = MXDrawableUtils.buildGradientDrawable(
                context, R.color.mx_dialog_color_background,
                contentCornerRadiusDP
            )
        } else {
            mxCardLay?.setBackgroundResource(R.drawable.mx_dialog_card_bg)
        }

        kotlin.run { // 位置设置
            val marginLeft = MXDialogUtils.dp2px(context, cardMarginDP.left)
            val marginTop = MXDialogUtils.dp2px(
                context,
                cardMarginDP.top
            ) + (if (includeStatusBarHeight) MXDialogUtils.getStatusBarHeight(context) else 0)
            val marginRight = MXDialogUtils.dp2px(context, cardMarginDP.right)
            val marginBottom = MXDialogUtils.dp2px(
                context,
                cardMarginDP.bottom
            ) + (if (includeNavigationBarHeight) MXDialogUtils.getNavigationBarHeight(context) else 0)

            mxRootLay?.setPadding(marginLeft, marginTop, marginRight, marginBottom)
        }

        kotlin.run {
            val color = dialogBackgroundColor
                ?: context.resources.getColor(R.color.mx_dialog_color_background_alpha)
            mxRootLay?.setBackgroundColor(color)
        }

        kotlin.run {
            mxRootLay?.gravity = position.gravity

            mxCardLay?.translationX =
                MXDialogUtils.dp2px(context, position.translationX ?: 0).toFloat()
            mxCardLay?.translationY =
                MXDialogUtils.dp2px(context, position.translationY ?: 0).toFloat()
        }
    }

    protected open fun showCancelBtn() = true
    protected open fun showActionBtn() = false

    private fun processCancelBtn() {
        val button = cancelBtn ?: return
        val visible = showCancelBtn() && isCancelable()
        if (!visible) {
            button.visibility = View.GONE
            return
        }
        button.visibility = View.VISIBLE
        button.text =
            cancelProp.text ?: context.resources.getString(R.string.mx_dialog_button_cancel_text)
        cancelProp.attachTextColor(cancelBtn, R.color.mx_dialog_color_text_cancel)
        cancelProp.attachTextSize(cancelBtn, R.dimen.mx_dialog_text_size_button)
        button.setOnClickListener { onBackPressed() }
        if (contentCornerRadiusDP >= 0) {
            button.background = MXDrawableUtils.buildGradientDrawable(
                context, R.color.mx_dialog_color_cancel,
                contentCornerRadiusDP
            )
        } else {
            button.setBackgroundResource(R.drawable.mx_dialog_btn_bg_cancel_corner)
        }
    }

    private fun processActionBtn() {
        val button = okBtn ?: return
        val visible = showActionBtn()
        if (!visible) {
            button.visibility = View.GONE
            return
        }

        button.visibility = View.VISIBLE
        button.text =
            actionProp.text ?: context.resources.getString(R.string.mx_dialog_button_action_text)
        actionProp.attachTextColor(okBtn, R.color.mx_dialog_color_text_action)
        actionProp.attachTextSize(okBtn, R.dimen.mx_dialog_text_size_button)
        button.setOnClickListener {
            dismiss()
            actionProp.onclick?.invoke()
        }
        if (contentCornerRadiusDP >= 0) {
            button.background = MXDrawableUtils.buildGradientDrawable(
                context, R.color.mx_dialog_color_action,
                contentCornerRadiusDP
            )
        } else {
            button.setBackgroundResource(R.drawable.mx_dialog_btn_bg_action_corner)
        }
    }

    override fun setTitle(title: CharSequence?) {
        titleStr = title

        initDialog()
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.resources.getString(titleId)

        initDialog()
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)

        initDialog()
    }

    fun setAdapt(adapt: ListAdapter) {
        listView?.adapter = adapt
    }

    fun setItemClick(call: (Int) -> Unit) {
        listView?.setOnItemClickListener { _, _, position, _ ->
            call.invoke(position)
        }
    }

    /**
     * 设置活动按钮
     */
    fun setActionBtn(
        text: CharSequence? = null,
        textColor: Int? = null,
        textSizeSP: Float? = null
    ) {
        actionProp.text = text
        actionProp.textColor = textColor
        actionProp.textSizeSP = textSizeSP

        initDialog()
    }

    /**
     * 设置取消按钮
     */
    fun setCancelBtn(
        text: CharSequence? = null,
        textColor: Int? = null,
        textSizeSP: Float? = null
    ) {
        cancelProp.text = text ?: context.resources.getString(R.string.mx_dialog_button_cancel_text)
        cancelProp.textColor = textColor
        cancelProp.textSizeSP = textSizeSP

        initDialog()
    }

    protected fun setActionClick(click: (() -> Unit)? = null) {
        actionProp.onclick = click

        initDialog()
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
    fun setContentCornerRadius(radiusDP: Float) {
        contentCornerRadiusDP = radiusDP

        initDialog()
    }

    /**
     * 设置内容最大宽高比
     */
    fun setContentMaxHeightRatio(ratioDP: Float) {
        contentMaxHeightRatioDP = ratioDP

        initDialog()
    }

    /**
     * 设置弹窗位置
     */
    fun setContentPosition(position: MXDialogPosition) {
        this.position = position

        initDialog()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     * @param margin 左/右/上/下 边框宽度
     * @param includeStatusBarHeight 上边框宽度是否加上状态栏高度
     * @param includeNavigationBarHeight 下边框宽度是否加上导航栏高度
     */
    fun setContentMargin(
        margin: Float,
        includeStatusBarHeight: Boolean = false,
        includeNavigationBarHeight: Boolean = false
    ) {
        cardMarginDP = RectF(margin, margin, margin, margin)
        this.includeStatusBarHeight = includeStatusBarHeight
        this.includeNavigationBarHeight = includeNavigationBarHeight

        initDialog()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     * @param horizontal 左/右 边框宽度
     * @param vertical 上/下 边框宽度
     * @param includeStatusBarHeight 上边框宽度是否加上状态栏高度
     * @param includeNavigationBarHeight 下边框宽度是否加上导航栏高度
     */
    fun setContentMargin(
        horizontal: Float, vertical: Float,
        includeStatusBarHeight: Boolean = false,
        includeNavigationBarHeight: Boolean = false
    ) {
        cardMarginDP = RectF(horizontal, vertical, horizontal, vertical)
        this.includeStatusBarHeight = includeStatusBarHeight
        this.includeNavigationBarHeight = includeNavigationBarHeight

        initDialog()
    }

    /**
     * 设置内容外边距
     * 单位：DP
     * @param left 左 边框宽度
     * @param top 上 边框宽度
     * @param right 右 边框宽度
     * @param bottom 下 边框宽度
     * @param includeStatusBarHeight 上边框宽度是否加上状态栏高度
     * @param includeNavigationBarHeight 下边框宽度是否加上导航栏高度
     */
    fun setContentMargin(
        left: Float, top: Float, right: Float, bottom: Float,
        includeStatusBarHeight: Boolean = true,
        includeNavigationBarHeight: Boolean = true
    ) {
        cardMarginDP = RectF(left, top, right, bottom)
        this.includeStatusBarHeight = includeStatusBarHeight
        this.includeNavigationBarHeight = includeNavigationBarHeight

        initDialog()
    }
}