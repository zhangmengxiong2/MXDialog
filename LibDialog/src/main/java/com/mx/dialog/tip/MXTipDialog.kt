package com.mx.dialog.tip

import android.content.Context
import android.widget.TextView
import com.mx.dialog.R

open class MXTipDialog(context: Context) :
    MXTipBaseDialog(context, R.layout.mx_dialog_tip_content) {
    private var msgStr: CharSequence? = null
    private var msgTxv: TextView? = null
    override fun initView() {
        super.initView()
        msgTxv = findViewById(R.id.msgTxv)
    }

    override fun initData() {
        super.initData()
        msgTxv?.text = msgStr
    }

    fun setMessage(message: CharSequence) {
        msgStr = message
        msgTxv?.text = msgStr
    }

    companion object {
        fun warn() {

        }
    }
}