package com.mx.dialog.tip

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.mx.dialog.BaseDialog
import com.mx.dialog.R
import com.mx.dialog.utils.ButtonProps

open class TipBaseDialog(context: Context, private val contentRes: Int? = null) :
    BaseDialog(context) {
    private var rootLay: RelativeLayout? = null
    private var cardLay: ViewGroup? = null
    private var contentLay: FrameLayout? = null
    private var titleTxv: TextView? = null
    private var cancelBtn: TextView? = null
    private var okBtn: TextView? = null

    private var titleStr: CharSequence? = null

    private var inActiveProp: ButtonProps? = null
    private var activeProp: ButtonProps? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mx_dialog_tip)
        val contentLay = findViewById<FrameLayout>(R.id.contentLay)
        contentLay.removeAllViews()
        contentRes?.let { layoutId ->
            View.inflate(context, layoutId, contentLay)
        }

        initView()
        initData()
    }

    protected open fun initView() {
        rootLay = findViewById(R.id.rootLay)
        cardLay = findViewById(R.id.cardLay)
        contentLay = findViewById(R.id.contentLay)
        titleTxv = findViewById(R.id.titleTxv)
        cancelBtn = findViewById(R.id.cancelBtn)
        okBtn = findViewById(R.id.okBtn)
    }

    protected open fun initData() {
        rootLay?.setOnClickListener {
            onBackPressed()
        }
        cardLay?.setOnClickListener { }
        titleTxv?.text = titleStr ?: "温馨提示"

        setButtonAction(cancelBtn, inActiveProp, "")
        setButtonAction(okBtn, activeProp, "确认")
    }

    fun setInActiveBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        color: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        inActiveProp = ButtonProps(visible, text ?: "取消", color) {
            // 先触发onCancelListener,再触发用户设置的回调
            dispatchOnCancelListener()
            onclick?.invoke()
        }

        setButtonAction(cancelBtn, inActiveProp, "")
    }

    override fun setTitle(title: CharSequence?) {
        titleStr = title

        titleTxv?.text = titleStr ?: "温馨提示"
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.getString(titleId)

        titleTxv?.text = titleStr ?: "温馨提示"
    }

    fun setActiveBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        color: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        activeProp = ButtonProps(visible, text ?: "确认", color, onclick)

        setButtonAction(okBtn, activeProp, "确认")
    }

    private fun setButtonAction(button: TextView?, inActiveProp: ButtonProps?, s: String) {
        button?.text = s
        if (inActiveProp != null) {
            button?.text = inActiveProp.text
            button?.visibility = if (inActiveProp.visible) View.VISIBLE else View.GONE
            inActiveProp.color?.let { button?.setTextColor(it) }
            button?.setOnClickListener {
                dismiss()
                inActiveProp.onclick?.invoke()
            }
        } else {
            button?.visibility = View.GONE
        }
    }
}