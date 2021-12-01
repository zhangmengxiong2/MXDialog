package com.mx.dialog.tip

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import com.mx.dialog.R

open class MXTipDialog(context: Context) : MXTipBaseDialog(context) {
    private var msgStr: CharSequence? = null
    private var msgTxv: TextView? = null
    private var tipScrollView: ScrollView? = null
    override fun generalContentView(parent: FrameLayout): View? {
        return LayoutInflater.from(context).inflate(R.layout.mx_dialog_tip_content, parent, false)
    }

    override fun initView() {
        super.initView()
        msgTxv = findViewById(R.id.msgTxv)
        tipScrollView = findViewById(R.id.tipScrollView)
    }

    override fun initData() {
        super.initData()
        msgTxv?.text = msgStr
    }

    fun setMessage(message: CharSequence?) {
        msgStr = message
        msgTxv?.text = msgStr
        msgTxv?.movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        /**
         * 需要用户确认的操作，无法返回且只能点击确认/取消
         * @param message 消息内容
         * @param title 标题 默认=“温馨提示”
         * @param actionButtonText 确认按钮文字
         * @param cancelButtonText 取消按钮文字
         * @param onActionClick 操作点击回调
         */
        fun confirm(
            context: Context,
            message: CharSequence,
            title: CharSequence? = null,
            actionButtonText: CharSequence? = null,
            cancelButtonText: CharSequence? = null,
            onActionClick: ((confirm: Boolean) -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setCancelable(false)
            dialog.setActionBtn(text = actionButtonText) { onActionClick?.invoke(true) }
            dialog.setCancelBtn(text = cancelButtonText) { onActionClick?.invoke(false) }
            dialog.setTipType(MXDialogType.WARN)
            dialog.show()
        }

        fun warn(
            context: Context,
            message: CharSequence,
            title: CharSequence? = null,
            dismissDelay: Int? = null,
            onActionClick: (() -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setDismissDelay(dismissDelay)
            dialog.setActionBtn(text = "确认") { onActionClick?.invoke() }
            dialog.setTipType(MXDialogType.WARN)
            dialog.show()
        }

        fun success(
            context: Context,
            message: CharSequence,
            title: CharSequence? = null,
            dismissDelay: Int? = null,
            onActionClick: (() -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setDismissDelay(dismissDelay)
            dialog.setActionBtn(text = "确认") { onActionClick?.invoke() }
            dialog.setTipType(MXDialogType.SUCCESS)
            dialog.show()
        }

        fun error(
            context: Context,
            message: CharSequence,
            title: CharSequence? = null,
            dismissDelay: Int? = null,
            onActionClick: (() -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setDismissDelay(dismissDelay)
            dialog.setActionBtn(text = "确认") { onActionClick?.invoke() }
            dialog.setTipType(MXDialogType.ERROR)
            dialog.show()
        }
    }
}