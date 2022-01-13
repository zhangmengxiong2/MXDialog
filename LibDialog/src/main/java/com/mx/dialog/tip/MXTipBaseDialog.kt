package com.mx.dialog.tip

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog
import com.mx.dialog.utils.MXButtonProps
import com.mx.dialog.utils.MXButtonType
import com.mx.dialog.views.MXRatioFrameLayout

abstract class MXTipBaseDialog(context: Context, fullScreen: Boolean = false) :
    MXBaseCardDialog(context, fullScreen) {
    private var btnLay: LinearLayout? = null
    private var tipTypeImg: ImageView? = null
    private var contentLay: MXRatioFrameLayout? = null
    private var titleTxv: TextView? = null
    private var delayTxv: TextView? = null
    private var cancelBtn: TextView? = null
    private var okBtn: TextView? = null

    private var titleStr: CharSequence? = null
    private var titleGravity: Int = Gravity.LEFT

    private var onCancelCall: (() -> Unit)? = null
    private var cancelProp: MXButtonProps? = null
    private var actionProp: MXButtonProps? = null


    private var buttonType = MXButtonType.Normal
    private var tipType = MXDialogType.NONE

    private var maxContentRatio: Float = 0f

    override fun getContentLayoutId(): Int {
        return R.layout.mx_content_tip
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentLay = findViewById(R.id.contentLay)
        // 从重构方法创建Content内容
        generalContentView(contentLay!!)?.let { view ->
            contentLay?.removeAllViews()
            contentLay?.addView(
                view,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }

        initView()
    }

    abstract fun generalContentView(parent: FrameLayout): View?

    protected open fun initView() {
        btnLay = findViewById(R.id.btnLay)
        tipTypeImg = findViewById(R.id.tipTypeImg)
        titleTxv = findViewById(R.id.titleTxv)
        delayTxv = findViewById(R.id.delayTxv)
        cancelBtn = findViewById(R.id.cancelBtn)
        okBtn = findViewById(R.id.okBtn)
    }

    override fun onDismissTicket(maxSecond: Int, remindSecond: Int) {
        delayTxv?.text = "$remindSecond s"
        delayTxv?.visibility = View.VISIBLE
    }

    override fun initDialog() {
        super.initDialog()

        if (titleTxv == null) return
        titleTxv?.text = titleStr ?: context.getString(R.string.mx_dialog_tip_title)
        titleTxv?.gravity = titleGravity

        contentLay?.setMaxHeightRatio(maxContentRatio)

        attachButton(cancelBtn, cancelProp, "")
        attachButton(okBtn, actionProp, "确认")

        if (cancelProp != null || actionProp != null) {
            btnLay?.visibility = View.VISIBLE

            val cornerDP = getCardBackgroundRadiusDP()
            MXButtonType.attach(buttonType, btnLay, cancelBtn, okBtn, cornerDP)
        } else {
            btnLay?.visibility = View.GONE
        }

        when (tipType) {
            MXDialogType.NONE -> {
                tipTypeImg?.visibility = View.GONE
            }
            MXDialogType.SUCCESS -> {
                tipTypeImg?.visibility = View.VISIBLE
                tipTypeImg?.setImageResource(R.drawable.mx_dialog_icon_success)
            }
            MXDialogType.WARN -> {
                tipTypeImg?.visibility = View.VISIBLE
                tipTypeImg?.setImageResource(R.drawable.mx_dialog_icon_warn)
            }
            MXDialogType.ERROR -> {
                tipTypeImg?.visibility = View.VISIBLE
                tipTypeImg?.setImageResource(R.drawable.mx_dialog_icon_error)
            }
        }
    }

    /**
     * 设置活动按钮
     */
    fun setActionBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        textColor: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        actionProp = MXButtonProps(visible, text ?: "确认", textColor, onclick)

        initDialog()
    }

    /**
     * 设置取消按钮
     * @param visible 按钮是否可见
     * @param text 按钮文字内容
     * @param textColor 文字颜色
     * @param onclick 取消按钮响应方法，
     * @see #setOnCancelListener(DialogInterface.OnCancelListener)
     */
    fun setCancelBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        textColor: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        onCancelCall = onclick
        cancelProp = MXButtonProps(visible, text ?: "取消", textColor) {
            // 先触发onCancelListener,再触发用户设置的回调
            onCancelCall?.invoke()
        }

        initDialog()
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        onCancelCall = { listener?.onCancel(this) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isCancelable()) {
            onCancelCall?.invoke()
        }
    }

    override fun setTitle(title: CharSequence?) {
        titleStr = title
        titleGravity = Gravity.LEFT
        initDialog()
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.getString(titleId)
        titleGravity = Gravity.LEFT
        initDialog()
    }

    fun setTitle(title: CharSequence?, gravity: Int = Gravity.LEFT) {
        titleStr = title
        titleGravity = gravity
        initDialog()
    }

    /**
     * 设置内容最大宽高比
     */
    fun setMaxContentRatio(ratio: Float) {
        maxContentRatio = ratio

        initDialog()
    }

    fun setTipType(type: MXDialogType?) {
        this.tipType = type ?: MXDialogType.NONE

        initDialog()
    }

    fun setButtonType(type: MXButtonType) {
        this.buttonType = type

        initDialog()
    }

    private fun attachButton(button: TextView?, inActiveProp: MXButtonProps?, s: String) {
        button?.text = s
        if (inActiveProp != null) {
            button?.text = inActiveProp.text
            button?.visibility = if (inActiveProp.visible) View.VISIBLE else View.GONE
            inActiveProp.textColor?.let { button?.setTextColor(it) }
            button?.setOnClickListener {
                dismiss()
                inActiveProp.onclick?.invoke()
            }
        } else {
            button?.visibility = View.GONE
        }
    }
}