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

open class MXTipDialog(context: Context) : MXTipBaseDialog(context) {
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
            cancelable: Boolean = true,
            onActionClick: ((confirm: Boolean) -> Unit)? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setCancelable(cancelable)
            dialog.setActionBtn(text = actionButtonText) { onActionClick?.invoke(true) }
            if (cancelable) {
                dialog.setCancelBtn(text = cancelButtonText) {
                    onActionClick?.invoke(false)
                }
                dialog.setOnCancelListener {
                    onActionClick?.invoke(false)
                }
            }
            dialog.setTipType(MXDialogType.WARN)
            dialog.show()
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
            context: Context, message: CharSequence, title: CharSequence? = null,
            actionButtonText: CharSequence? = null,
            onActionClick: ((confirm: Boolean) -> Unit)? = null,
            cancelable: Boolean = true,
            cancelButtonText: CharSequence? = null,
            onCancelListener: (() -> Unit)? = null,
            dismissDelay: Int? = null, dialogType: MXDialogType? = null
        ) {
            val dialog = MXTipDialog(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setCancelable(cancelable)
            dialog.setDismissDelay(dismissDelay)

            if (cancelable) {
                dialog.setCancelBtn(
                    visible = cancelButtonText != null,
                    text = cancelButtonText
                ) { onActionClick?.invoke(false) }

                dialog.setOnCancelListener(onCancelListener)
            }

            dialog.setActionBtn(
                text = actionButtonText
                    ?: context.resources.getString(R.string.mx_dialog_button_action_text)
            ) { onActionClick?.invoke(true) }
            dialog.setTipType(dialogType)

            dialog.show()
        }
    }
}