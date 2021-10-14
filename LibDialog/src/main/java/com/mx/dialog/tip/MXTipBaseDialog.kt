package com.mx.dialog.tip

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.mx.dialog.MXBaseDialog
import com.mx.dialog.R
import com.mx.dialog.utils.MXButtonProps

open class MXTipBaseDialog(context: Context, private val contentRes: Int? = null) :
    MXBaseDialog(context) {
    private val mHandler = Handler(Looper.getMainLooper())
    private var rootLay: FrameLayout? = null
    private var cardLay: ViewGroup? = null
    private var btnLay: ViewGroup? = null
    private var tipTypeImg: ImageView? = null
    private var contentLay: FrameLayout? = null
    private var titleTxv: TextView? = null
    private var delayTxv: TextView? = null
    private var cancelBtn: TextView? = null
    private var okBtn: TextView? = null

    private var titleStr: CharSequence? = null

    private var inActiveProp: MXButtonProps? = null
    private var activeProp: MXButtonProps? = null

    private var dismissDelay: Int? = null
    private var gravity = MXTipGravity.CENTER
    private var tipType = MXTipType.NONE

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

        processDelay()
    }

    protected open fun initView() {
        rootLay = findViewById(R.id.rootLay)
        cardLay = findViewById(R.id.cardLay)
        btnLay = findViewById(R.id.btnLay)
        tipTypeImg = findViewById(R.id.tipTypeImg)
        contentLay = findViewById(R.id.contentLay)
        titleTxv = findViewById(R.id.titleTxv)
        delayTxv = findViewById(R.id.delayTxv)
        cancelBtn = findViewById(R.id.cancelBtn)
        okBtn = findViewById(R.id.okBtn)
    }

    protected open fun initData() {
        if (rootLay == null) return

        rootLay?.setOnClickListener {
            onBackPressed()
        }
        cardLay?.setOnClickListener { }
        titleTxv?.text = titleStr ?: "温馨提示"

        setButtonAction(cancelBtn, inActiveProp, "")
        setButtonAction(okBtn, activeProp, "确认")
        if (inActiveProp != null || activeProp != null) {
            btnLay?.visibility = View.VISIBLE
        } else {
            btnLay?.visibility = View.GONE
        }

        val lp = (cardLay?.layoutParams as FrameLayout.LayoutParams)
        lp.gravity = when (gravity) {
            MXTipGravity.TOP -> Gravity.NO_GRAVITY
            MXTipGravity.CENTER -> Gravity.CENTER_VERTICAL
            MXTipGravity.BOTTOM -> Gravity.BOTTOM
        }
        cardLay?.layoutParams = lp

        when (tipType) {
            MXTipType.NONE -> {
                tipTypeImg?.visibility = View.GONE
            }
            MXTipType.SUCCESS -> {
                tipTypeImg?.visibility = View.VISIBLE
                tipTypeImg?.setImageResource(R.drawable.mx_dialog_icon_success)
            }
            MXTipType.WARN -> {
                tipTypeImg?.visibility = View.VISIBLE
                tipTypeImg?.setImageResource(R.drawable.mx_dialog_icon_warn)
            }
            MXTipType.ERROR -> {
                tipTypeImg?.visibility = View.VISIBLE
                tipTypeImg?.setImageResource(R.drawable.mx_dialog_icon_error)
            }
        }
    }

    fun setActiveBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        color: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        activeProp = MXButtonProps(visible, text ?: "确认", color, onclick)

        initData()
    }

    fun setInActiveBtn(
        visible: Boolean = true,
        text: CharSequence? = null,
        color: Int? = null,
        onclick: (() -> Unit)? = null
    ) {
        inActiveProp = MXButtonProps(visible, text ?: "取消", color) {
            // 先触发onCancelListener,再触发用户设置的回调
            dispatchOnCancelListener()
            onclick?.invoke()
        }

        initData()
    }

    override fun setTitle(title: CharSequence?) {
        titleStr = title

        initData()
    }

    override fun setTitle(titleId: Int) {
        titleStr = context.getString(titleId)

        initData()
    }

    fun setGravity(gravity: MXTipGravity) {
        this.gravity = gravity

        initData()
    }

    fun setTipType(type: MXTipType) {
        this.tipType = type

        initData()
    }

    fun setDismissDelay(second: Int?) {
        mHandler.removeCallbacksAndMessages(null)
        dismissDelay = second
        processDelay()
    }

    private fun processDelay() {
        val delay = dismissDelay
        if (delay != null) {
            mHandler.postDelayed({
                if (isShowing) {
                    dismiss()
                }
            }, delay * 1000L)
            delayTxv?.text = "$delay 秒后消失"
            delayTxv?.visibility = View.VISIBLE
        } else {
            delayTxv?.visibility = View.GONE
            mHandler.removeCallbacksAndMessages(null)
        }
    }

    private fun setButtonAction(button: TextView?, inActiveProp: MXButtonProps?, s: String) {
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