package com.mx.dialog.tip

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mx.dialog.R
import com.mx.dialog.base.MXBaseCardDialog
import com.mx.dialog.utils.MXButtonProps
import com.mx.dialog.views.MaxHeightFrameLayout

open class MXTipBaseDialog(context: Context) : MXBaseCardDialog(context) {
    private var btnLay: ViewGroup? = null
    private var tipTypeImg: ImageView? = null
    private var contentLay: MaxHeightFrameLayout? = null
    private var titleTxv: TextView? = null
    private var delayTxv: TextView? = null
    private var cancelBtn: TextView? = null
    private var okBtn: TextView? = null

    private var titleStr: CharSequence? = null
    private var titleGravity: Int = Gravity.LEFT

    private var cancelProp: MXButtonProps? = null
    private var actionProp: MXButtonProps? = null

    private var tipType = MXDialogType.NONE

    private var maxContentRatio: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_tip)

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

    override fun onStart() {
        super.onStart()
        initData()
    }

    protected open fun generalContentView(parent: FrameLayout): View? = null

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

    protected open fun initData() {
        if (titleTxv == null) return
        titleTxv?.text = titleStr ?: "温馨提示"
        titleTxv?.gravity = titleGravity

        contentLay?.setMaxHeightRatio(maxContentRatio)

        attachButton(cancelBtn, cancelProp, "")
        attachButton(okBtn, actionProp, "确认")
        if (cancelProp != null || actionProp != null) {
            btnLay?.visibility = View.VISIBLE
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

        initData()
    }

    /**
     * 设置取消按钮
     */
    fun setCancelBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        textColor: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        cancelProp = MXButtonProps(visible, text ?: "取消", textColor) {
            // 先触发onCancelListener,再触发用户设置的回调
            dispatchOnCancelListener()
            onclick?.invoke()
        }

        initData()
    }

    override fun setTitle(title: CharSequence?) {
        titleStr = title
        titleGravity = Gravity.LEFT
        initData()
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.getString(titleId)
        titleGravity = Gravity.LEFT
        initData()
    }

    fun setTitle(title: CharSequence?, gravity: Int = Gravity.LEFT) {
        titleStr = title
        titleGravity = gravity
        initData()
    }

    fun setMaxContentRatio(ratio: Float) {
        maxContentRatio = ratio

        initData()
    }

    fun setTipType(type: MXDialogType) {
        this.tipType = type

        initData()
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