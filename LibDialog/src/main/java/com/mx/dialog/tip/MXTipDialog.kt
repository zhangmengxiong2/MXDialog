package com.mx.dialog.tip

import android.content.Context
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import com.mx.dialog.R

open class MXTipDialog(context: Context) : MXTipBaseDialog(context) {
    private var msgTypeface: Typeface? = null
    private var msgStr: CharSequence? = null
    private var msgColor: Int? = null
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
    }

    fun setMessage(
        message: CharSequence?,
        textColor: Int? = null,
        typeface: Typeface? = null
    ) {
        msgStr = message
        msgColor = textColor
        msgTypeface = typeface

        initDialog()
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
            showCancelButton: Boolean = true,
            onActionClick: ((confirm: Boolean) -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setCancelable(false)
            dialog.setActionBtn(text = actionButtonText) { onActionClick?.invoke(true) }
            dialog.setCancelBtn(
                text = cancelButtonText,
                visible = showCancelButton
            ) { onActionClick?.invoke(false) }
            dialog.setTipType(MXDialogType.WARN)
            dialog.show()
        }

        fun warn(
            context: Context,
            message: CharSequence,
            title: CharSequence? = null,
            actionButtonText: CharSequence? = null,
            dismissDelay: Int? = null,
            dialogType: MXDialogType? = null,
            onActionClick: (() -> Unit)? = null
        ) {
            show(
                context,
                message,
                title,
                dialogType = dialogType ?: MXDialogType.NONE,
                actionButtonText = actionButtonText ?: "确认",
                dismissDelay = dismissDelay,
                onActionClick = { confirm ->
                    if (confirm) onActionClick?.invoke()
                }
            )
        }

        /**
         * @param message 内容
         * @param title 标题
         * @param actionButtonText 活动按钮文字
         * @param cancelButtonText 取消按钮文字
         * @param dismissDelay x秒后弹窗消失
         * @param cancelable 是否响应返回按钮、是否点击空白处消失
         * @param dialogType Icon类型
         * @param onActionClick 按钮点击响应
         */
        fun show(
            context: Context,
            message: CharSequence,
            title: CharSequence? = null,
            actionButtonText: CharSequence? = null,
            cancelButtonText: CharSequence? = null,
            dismissDelay: Int? = null,
            cancelable: Boolean? = null,
            dialogType: MXDialogType? = null,
            onActionClick: ((confirm: Boolean) -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setCancelable(cancelable ?: true)
            dialog.setDismissDelay(dismissDelay)

            dialog.setCancelBtn(
                visible = cancelButtonText != null,
                text = cancelButtonText
            ) { onActionClick?.invoke(false) }

            dialog.setActionBtn(
                visible = true,
                text = actionButtonText ?: "确认"
            ) { onActionClick?.invoke(true) }
            dialog.setTipType(dialogType)

            dialog.show()
        }
    }
}