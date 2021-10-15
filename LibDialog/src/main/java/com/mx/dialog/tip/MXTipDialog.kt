package com.mx.dialog.tip

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mx.dialog.R

open class MXTipDialog(context: Context) : MXTipBaseDialog(context) {
    private var msgStr: CharSequence? = null
    private var msgTxv: TextView? = null
    override fun generalContentView(parent: FrameLayout): View? {
        return LayoutInflater.from(context).inflate(R.layout.mx_dialog_tip_content, parent, false)
    }

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
        msgTxv?.movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        /**
         * 需要用户确认的操作，无法返回且只能点击确认/取消
         */
        fun confirm(
            context: Context,
            message: String,
            onOkClick: ((confirm: Boolean) -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setMessage(message)
            dialog.setCancelable(false)
            dialog.setActionBtn(text = "确认", onclick = { onOkClick?.invoke(true) })
            dialog.setCancelBtn(onclick = { onOkClick?.invoke(false) })
            dialog.setTipType(MXDialogType.WARN)
            dialog.show()
        }

        fun warn(context: Context, message: String, dismissDelay: Int? = null) {
            val dialog = MXTipDialog(context)
            dialog.setMessage(message)
            dialog.setDismissDelay(dismissDelay)
            dialog.setActionBtn(text = "确认")
            dialog.setTipType(MXDialogType.WARN)
            dialog.show()
        }

        fun success(context: Context, message: String, dismissDelay: Int? = null) {
            val dialog = MXTipDialog(context)
            dialog.setMessage(message)
            dialog.setDismissDelay(dismissDelay)
            dialog.setActionBtn(text = "确认")
            dialog.setTipType(MXDialogType.SUCCESS)
            dialog.show()
        }

        fun error(context: Context, message: String, dismissDelay: Int? = null) {
            val dialog = MXTipDialog(context)
            dialog.setMessage(message)
            dialog.setDismissDelay(dismissDelay)
            dialog.setActionBtn(text = "确认")
            dialog.setTipType(MXDialogType.ERROR)
            dialog.show()
        }
    }
}