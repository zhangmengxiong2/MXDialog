package com.mx.dialog.tip

import android.content.Context
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import com.mx.dialog.R

open class MXTipDialog(context: Context, fullScreen: Boolean = false) :
    MXTipBaseDialog(context, fullScreen) {
    private var msgTypeface: Typeface? = null
    private var msgStr: CharSequence? = null
    private var msgColor: Int? = null
    private var msgGravity: Int = Gravity.LEFT or Gravity.CENTER_VERTICAL
    private var msgTxv: TextView? = null
    private var tipScrollView: ScrollView? = null
    override fun generalContentView(parent: FrameLayout): View? {
        return LayoutInflater.from(context).inflate(R.layout.mx_content_tip_textview, parent, false)
    }

    override fun initView() {
        super.initView()
        msgTxv = findViewById(R.id.msgTxv)
        tipScrollView = findViewById(R.id.tipScrollView)
    }

    override fun initDialog() {
        super.initDialog()

        msgTxv?.text = msgStr
        msgTxv?.setTextColor(msgColor ?: context.resources.getColor(R.color.mx_dialog_color_text))
        msgTxv?.typeface = msgTypeface ?: Typeface.DEFAULT
        msgTxv?.movementMethod = LinkMovementMethod.getInstance()
        msgTxv?.gravity = msgGravity
    }

    fun setMessage(
        message: CharSequence?,
        textColor: Int? = null,
        typeface: Typeface? = null,
        gravity: Int? = null
    ) {
        msgStr = message
        msgColor = textColor
        msgTypeface = typeface
        msgGravity = gravity ?: (Gravity.LEFT or Gravity.CENTER_VERTICAL)

        initDialog()
    }
}