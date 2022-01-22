package com.mx.dialog.list

import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseDialog
import com.mx.dialog.tip.MXDialogPosition
import com.mx.dialog.utils.MXDialogUtils
import com.mx.dialog.utils.MXDrawableUtils
import com.mx.dialog.views.MXRatioFrameLayout

open class MXListDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseDialog(context, fullScreen) {
    private var onItemClick: ((Int) -> Unit)? = null
    private var closeOnTouchOutside: Boolean = true
    private var includeStatusBarHeight: Boolean = false
    private var includeNavigationBarHeight: Boolean = false

    private var titleStr: CharSequence? = null

    private var dialogBackgroundColor: Int? = null
    private var contentCornerRadiusDP = 12f
    private var contentMaxHeightRatioDP = 0.8f
    private var cardMarginDP = RectF(15f, 15f, 15f, 15f)
    private var position = MXDialogPosition.BOTTOM
    private var mxRootLay: LinearLayout? = null
    private var mxCardLay: ViewGroup? = null
    private var contentLay: MXRatioFrameLayout? = null
    private var cancelBtn: TextView? = null
    private var titleTxv: TextView? = null
    private var listView: ListView? = null

    private val listAdapt by lazy { MXListItemAdapt(context) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_list)
        initView()
    }

    private fun initView() {
        if (mxRootLay == null) mxRootLay = findViewById(R.id.mxRootLay)
        if (mxCardLay == null) mxCardLay = findViewById(R.id.mxCardLay)
        if (contentLay == null) contentLay = findViewById(R.id.contentLay)
        if (cancelBtn == null) cancelBtn = findViewById(R.id.cancelBtn)
        if (titleTxv == null) titleTxv = findViewById(R.id.titleTxv)
        if (listView == null) listView = findViewById(R.id.listView)
    }

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
        if (listView == null) return
        listView?.adapter = listAdapt
        listView?.setOnItemClickListener { parent, view, position, id ->
            onItemClick?.invoke(position)
            dismiss()
        }

        contentLay?.setMaxHeightRatio(contentMaxHeightRatioDP)

        if (titleStr != null) {
            titleTxv?.text = titleStr
            titleTxv?.visibility = View.VISIBLE
        } else {
            titleTxv?.visibility = View.GONE
        }

        mxRootLay?.setOnClickListener {
            if (closeOnTouchOutside) {
                onBackPressed()
            }
        }
        mxCardLay?.setOnClickListener { }
        cancelBtn?.setOnClickListener { onBackPressed() }
        if (isCancelable()) {
            cancelBtn?.visibility = View.VISIBLE
        } else {
            cancelBtn?.visibility = View.GONE
        }

        if (contentCornerRadiusDP > 0) {
            mxCardLay?.background = MXDrawableUtils.buildGradientDrawable(
                context, context.resources.getColor(R.color.mx_dialog_color_background),
                contentCornerRadiusDP
            )
            cancelBtn?.background = MXDrawableUtils.buildGradientDrawable(
                context, context.resources.getColor(R.color.mx_dialog_color_background),
                contentCornerRadiusDP
            )
        } else {
            mxCardLay?.setBackgroundResource(R.drawable.mx_dialog_bg_select_cancel)
            cancelBtn?.setBackgroundResource(R.drawable.mx_dialog_bg_select_cancel)
        }

        kotlin.run { // 位置设置
            val marginLeft = MXDialogUtils.dp2px(context, cardMarginDP.left)
            val marginTop = MXDialogUtils.dp2px(
                context,
                cardMarginDP.top
            ) + if (includeStatusBarHeight) MXDialogUtils.getStatusBarHeight(context) else 0
            val marginRight = MXDialogUtils.dp2px(context, cardMarginDP.right)
            val marginBottom = MXDialogUtils.dp2px(
                context,
                cardMarginDP.bottom
            ) + if (includeNavigationBarHeight) MXDialogUtils.getNavigationBarHeight(context) else 0

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

    fun setItems(list: List<String>, onItemClick: ((index: Int) -> Unit)? = null) {
        listAdapt.list.clear()
        listAdapt.list.addAll(list)
        listAdapt.notifyDataSetChanged()
        this.onItemClick = onItemClick

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